package com.yysports.controller;

import com.yysports.service.ItemGroupReportService;
import com.yysports.service.ItemReportService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by samchu on 2016/11/15.
 */
@Slf4j
@Api(tags = "Report")
@RestController
@RequestMapping(value = "api")
public class ItemGroupReportController {

    @Autowired
    private ItemGroupReportService itemGroupReportService;

    @ApiOperation(value = "取得報表", notes = "下載商品集管理報表", produces = "application/octet-stream")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "long", paramType = "query",
                    value = "商品集编号", required = false),
            @ApiImplicitParam(name = "itemGroupName", dataType = "string", paramType = "query",
                    value = "商品集名称", required = false),
            @ApiImplicitParam(name = "shopIdPmt", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "线上店铺編號", required = false),
            @ApiImplicitParam(name = "brand", dataType = "string", paramType = "query",
                    value = "品牌名称", required = false),
            @ApiImplicitParam(name = "isSpecial", dataType = "int", paramType = "query",
                    value = "业务类型", required = false)
    })
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Byte excel資料", response = String.class)})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "v1/ItemGroupReport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public HttpEntity<byte[]> getItemGroupReport(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "itemGroupName", required = false) String itemGroupName,
            @RequestParam(name = "shopIdPmt", required = false) List<Long> shopIdPmt,
            @RequestParam(name = "brand", required = false) String brand,
            @RequestParam(name = "isSpecial", required = false) Integer isSpecial) throws Exception {
        log.info("接收查詢 id={}, itemGroupName={}, shopIdPmt={}, brand={}, isSpecial={}", id, itemGroupName, shopIdPmt, brand, isSpecial);
        ByteArrayOutputStream baos = itemGroupReportService.genExcel(id, itemGroupName, shopIdPmt, brand, isSpecial);
        return ResponseEntity
                .ok()
                .contentLength(baos.size())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename = ItemGroupReport.xlsx")
                .body(baos.toByteArray());
    }
}
