package com.babycare.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.babycare.model.BaseModel;
import com.babycare.model.Error;
import com.babycare.model.ResultList;
import com.babycare.model.common.ResultListEx;
import com.babycare.model.entity.ContentEntity;
import com.babycare.model.payload.Content;
import com.babycare.model.response.pager.PagerContent;
import com.babycare.service.IContentService;

@RestController(value = "contentController")
@RequestMapping("content")
public class ContentController {
	private final Logger LOG = LoggerFactory.getLogger(ContentController.class);
	@Autowired
	@Qualifier("contentService")
	private IContentService contentService;

	@PostMapping(value = "/add", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> addContent(@RequestBody Content body) {
		BaseModel model = contentService.addContent(body);
		return Response(model);
	}

	@PostMapping(value = "/update", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> updateContent(@RequestBody Content body) {
		BaseModel model = contentService.updateContent(body);
		return Response(model);
	}

	@PostMapping(value = "/get/by/id", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> getContentById(@RequestBody Content body) {
		BaseModel model = contentService.getContentById(body);
		return Response(model);
	}

	@PostMapping(value = "/get/by/contenttype", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> getByContentTypeId(@RequestBody Content body) {
		BaseModel model = contentService.getByContentTypeId(body);
		return Response(model);
	}

	// @PostMapping(value = "/get/example/pager", params = { "page", "size" },
	// headers = "Accept=application/json", produces = "application/json")
	@PostMapping(value = "/get/example/pager", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<Object> getByContentPager(@RequestBody Content body,
			@RequestParam(value = "page", required = false) final Integer page,
			@RequestParam(value = "size", required = false) final Integer size) {
		//LOG.debug("PHUONG Params page {}, size {}", page, size);
		if (page != null && page!= -1 && size != null && size != -1) {
			Example<ContentEntity> example = Example.of(new ContentEntity(body));
			Page<ContentEntity> retsult = contentService.findExamplePaginated(example, page, size);
			return new ResponseEntity<>(retsult, HttpStatus.OK);
		} else {
			List<ContentEntity> list = contentService.findAll();
			PagerContent<ContentEntity> contents = new PagerContent<>();
			contents.setContent(list);
			return new ResponseEntity<>(contents, HttpStatus.OK);
		}
	}

	/*
	 * @PostMapping(value = "/get/example/pager", params = { "page", "size" },
	 * headers = "Accept=application/json", produces = "application/json")
	 * public @ResponseBody ResponseEntity<PagerContent<ContentEntity>>
	 * getByContentTypeId(@RequestBody Content body, @RequestParam( "page" ) int
	 * page, @RequestParam( "size" ) int size) { Example<ContentEntity> example =
	 * Example.of(new ContentEntity(body)); Page<ContentEntity> retsult =
	 * contentService.findExamplePaginated(example, page, size);
	 * PagerContent<ContentEntity> pc = new PagerContent<>();
	 * 
	 * pc.setContent(retsult.getContent()); pc.setSort(new Sort());
	 * 
	 * pc.getSort().setSorted(retsult.getSort().isSorted());
	 * pc.getSort().setUnsorted(retsult.getSort().isUnsorted());
	 * pc.setFirst(pc.getFirst()); pc.setLast(retsult.isLast());
	 * pc.setNumber(retsult.getNumber());
	 * pc.setNumberOfElements(retsult.getNumber()); pc.setSize(retsult.getSize());
	 * pc.setTotalElements(retsult.getTotalElements());
	 * pc.setTotalPages(retsult.getTotalPages()); return new ResponseEntity<>(pc,
	 * HttpStatus.OK); }
	 */

	
	@PostMapping(value = "/get/by/week/contentypeid", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> getContentByWeekAndContentTypeId(
			@RequestParam(value = "weekNumber", required = false) final Integer weekNumber, 
			@RequestParam(value = "contentTypeId", required = false) final Long contentTypeId) {
		BaseModel model = contentService.getContentByWeekAndContentTypeId(weekNumber, contentTypeId);
		return Response(model);
	}

	@PostMapping(value = "/get/by/week", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> getContentByWeekAndContentTypeId(
			@RequestParam(value = "weekNumber", required = false) final Integer weekNumber) {
		BaseModel model = contentService.getContentByWeek(weekNumber);
		return Response(model);
	}

	@PostMapping(value = "/get/by/contentypeid/childid", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> getContentByContentTypeIdChildId(
			@RequestParam(value = "contentTypeId", required = false) final Long contentTypeId ,
			@RequestParam(value = "childId", required = false) final Long childId) {
		BaseModel model = contentService.getContentByContentTypeIdChildId(contentTypeId, childId);
		return Response(model);
	}

	@PostMapping(value = "/get/by/childid", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> getContentByChildId(
			@RequestParam(value = "childId", required = false) final Long childId) {
		BaseModel model = contentService.getContentByChildId(childId);
		return Response(model);
	}

	private @ResponseBody ResponseEntity<BaseModel> Response(BaseModel model) {
		if (model instanceof ContentEntity) {
			return new ResponseEntity<BaseModel>(model, HttpStatus.OK);
		} else if (model instanceof Error) {
			return new ResponseEntity<BaseModel>(model, HttpStatus.CONFLICT);
		} else if (model instanceof ResultList) {
			return new ResponseEntity<BaseModel>(model, HttpStatus.OK);
		} else if (model instanceof ResultListEx) {
			return new ResponseEntity<BaseModel>(model, HttpStatus.OK);
		}else {
			return new ResponseEntity<BaseModel>((Content) null, HttpStatus.CONFLICT);
		}
	}
}
