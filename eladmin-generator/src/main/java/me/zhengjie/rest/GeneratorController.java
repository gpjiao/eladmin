package me.zhengjie.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;

import me.zhengjie.domain.vo.TableField;
import me.zhengjie.domain.vo.TableInfo;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.http.ApiResponse;
import me.zhengjie.service.GenConfigService;
import me.zhengjie.service.GeneratorService;

import java.util.List;

/**
 * @author jie
 * @date 2019-01-02
 */
@RestController
@RequestMapping("api")
public class GeneratorController {

    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private GenConfigService genConfigService;

    @Value("${generator.enabled}")
    private Boolean generatorEnabled;

    /**
     * 查询数据库元数据
     * @param name
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/generator/tables")
    public ApiResponse<List<TableInfo>> getTables(@RequestParam(defaultValue = "") String name,
        @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size)
    {
        IPage<TableInfo> iPage = generatorService.listTables(page, size, name);
        return ApiResponse.code(HttpStatus.OK).page(iPage);
    }

    /**
     * 查询表内元数据
     * @param tableName
     * @return
     */
    @GetMapping(value = "/generator/columns")
    public ApiResponse<List<TableField>> getTables(@RequestParam String tableName)
    {
        List<TableField> records = generatorService.listFields(tableName);
        return ApiResponse.code(HttpStatus.OK).body(records);
    }

    /**
     * 生成代码
     * @param columnInfos
     * @return
     */
    @PostMapping(value = "/generator")
    public ApiResponse<?> generator(@RequestBody List<TableField> columnInfos, @RequestParam String tableName)
    {
        if (!generatorEnabled)
        {
            throw new BadRequestException("此环境不允许生成代码！");
        }
        generatorService.generator(columnInfos, genConfigService.find(), tableName);
        return ApiResponse.code(HttpStatus.OK).build();
    }
}
