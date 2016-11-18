package com.yysports.controller;

import com.yysports.service.ItemReportService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.io.ByteArrayOutputStream;

/**
 * Created by samchu on 2016/11/15.
 */
@Slf4j
@Api(tags = "Report")
@RestController
@RequestMapping(value = "api")
public class ItemReportController {

    @Autowired
    private ItemReportService itemReportService;

    @ApiOperation(value = "取得報表", notes = "下載商品管理報表", produces = "application/octet-stream")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "long", paramType = "query",
                    value = "编号", required = false),
            @ApiImplicitParam(name = "upcCode", dataType = "string", paramType = "query",
                    value = "货号", required = false),
            @ApiImplicitParam(name = "itemName", dataType = "string", paramType = "query",
                    value = "商品名称", required = false),
            @ApiImplicitParam(name = "type", dataType = "string", paramType = "query",
                    value = "类型", required = false),
            @ApiImplicitParam(name = "itemGroupName", dataType = "string", paramType = "query",
                    value = "商品集名称", required = false)
    })
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Byte excel資料", response = String.class)})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "v1/ItemReport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public HttpEntity<byte[]> getItemReport(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "upcCode", required = false) String upcCode,
            @RequestParam(name = "itemName", required = false) String itemName,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "itemGroupName", required = false) String itemGroupName) throws Exception {
        log.debug("接收查詢 id={}, upcCode={}, itemName={},type={}, itemGroupName={}", id, upcCode, itemName, type, itemGroupName);
        ByteArrayOutputStream baos = itemReportService.genExcel(id, upcCode, itemName, type, itemGroupName);
        return ResponseEntity
                .ok()
                .contentLength(baos.size())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename = ItemReport.xlsx")
                .body(baos.toByteArray());
    }

}
