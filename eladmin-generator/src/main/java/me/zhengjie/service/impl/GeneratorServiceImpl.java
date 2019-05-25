package me.zhengjie.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateException;
import cn.hutool.extra.template.TemplateUtil;
import me.zhengjie.domain.GenConfig;
import me.zhengjie.domain.vo.TableField;
import me.zhengjie.domain.vo.TableInfo;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.mapper.TableFieldDao;
import me.zhengjie.mapper.TableInfoDao;
import me.zhengjie.service.GeneratorService;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * @author jie
 * @date 2019-01-02
 */
@Service
public class GeneratorServiceImpl implements GeneratorService
{
    
    @Autowired
    private TableInfoDao tableInfoDao;
    
    @Autowired
    private TableFieldDao tableFieldDao;
    
    @Autowired
    private DruidDataSource druidDataSource;
    
    @Override
    public IPage<TableInfo> listTables(Integer pageNum, Integer pageSize, String tableName)
    {
        return tableInfoDao.list(new Page<TableInfo>(pageNum, pageSize), tableName);
    }
    
    @Override
    public List<TableField> listFields(String tableName)
    {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(tableName))
        {
            List<TableField> records = tableFieldDao.list(tableName);
            if (CollectionUtils.isNotEmpty(records))
            {
                MySqlTypeConvert mySqlTypeConvert = new MySqlTypeConvert();
                GlobalConfig globalConfig = new GlobalConfig();
                for (TableField field : records)
                {
                    field.setPropertyName(NamingStrategy.underlineToCamel(field.getColumnName()));
                    if (org.apache.commons.lang3.StringUtils.isBlank(field.getColumnComment()))
                    {
                        field.setColumnComment(field.getPropertyName());
                    }
                    field.setPropertyType(
                        mySqlTypeConvert.processTypeConvert(globalConfig, field.getDataType()).getType());
                }
            }
            return records;
        }
        return null;
    }
    
    @Override
    public void generator(List<TableField> tableFields, GenConfig genConfig, String tableName)
    {
        if (genConfig.getId() == null)
        {
            throw new BadRequestException("请先配置生成器");
        }
        try
        {
            generateJava(genConfig, tableName);
            generateFront(tableFields, genConfig, tableName);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    private void generateJava(GenConfig genConfig, String tableName)
    {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(druidDataSource.getUrl());
        // dsc.setSchemaName("public");
        dsc.setDriverName(druidDataSource.getDriverClassName());
        dsc.setUsername(druidDataSource.getUsername());
        dsc.setPassword(druidDataSource.getPassword());
        mpg.setDataSource(dsc);
        
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor(genConfig.getAuthor());
        gc.setOpen(false);
        gc.setBaseColumnList(true);
        gc.setBaseResultMap(true);
        gc.setFileOverride(genConfig.getCover());
        mpg.setGlobalConfig(gc);
        
        // 包配置
        PackageConfig pc = new PackageConfig();
        // 模块名
        pc.setModuleName(genConfig.getModuleName());
        // 包名
        pc.setParent(genConfig.getPack());
        mpg.setPackageInfo(pc);
        
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig()
        {
            @Override
            public void initMap()
            {
                // to do nothing
            }
        };
        
        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";
        
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath)
        {
            @Override
            public String outputFile(com.baomidou.mybatisplus.generator.config.po.TableInfo tableInfo)
            {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName() + "/"
                    + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        
        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        
        // 配置自定义输出模板
        // 指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        templateConfig.setService("template/generator/admin/service.java");
        templateConfig.setServiceImpl("template/generator/admin/serviceImpl.java");
        templateConfig.setController("template/generator/admin/controller.java");
        
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);
        
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // strategy.setSuperControllerClass("com.baomidou.ant.common.BaseController");
        strategy.setInclude(tableName);
        // strategy.setSuperEntityColumns("id");
        // strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
    
    /**
     * 
     * @title 生成前端代码
     * @description <功能详细描述>
     * @date 2019年3月15日 下午4:29:52
     * @param tableFields 表元数据
     * @param genConfig 生成代码的参数配置，如包路径，作者
     * @param tableName
     * @throws IOException
     */
    private void generateFront(List<TableField> tableFields, GenConfig genConfig, String tableName)
        throws IOException
    {
        String entityName = NamingStrategy.underlineToCamel(tableName);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("moduleName", genConfig.getModuleName());
        map.put("changeClassName", entityName);
        map.put("hasTimestamp", false);
        map.put("hasQuery", false);
        
        List<Map<String, Object>> columns = new ArrayList<>();
        List<Map<String, Object>> queryColumns = new ArrayList<>();
        for (TableField column : tableFields)
        {
            Map<String, Object> listMap = new HashMap<String, Object>();
            listMap.put("columnComment", column.getColumnComment());
            
            String colType = column.getPropertyType();
            if (DbColumnType.LOCAL_DATE_TIME.getType().equals(colType)
                || DbColumnType.TIMESTAMP.getType().equals(colType))
            {
                map.put("hasTimestamp", true);
            }
            listMap.put("columnType", colType);
            listMap.put("columnName", column.getColumnName());
            listMap.put("columnShow", column.getColumnShow());
            listMap.put("changeColumnName", column.getPropertyName());
            
            if (StringUtils.isNotBlank(column.getQuery()))
            {
                map.put("hasQuery", true);
                queryColumns.add(listMap);
            }
            columns.add(listMap);
        }
        map.put("columns", columns);
        map.put("queryColumns", queryColumns);
        TemplateEngine engine = TemplateUtil.createEngine(new cn.hutool.extra.template.TemplateConfig("template",
            cn.hutool.extra.template.TemplateConfig.ResourceMode.CLASSPATH));
        
        // 生成前端代码
        Map<String, String> frontTemplage = new TreeMap<String, String>();
        frontTemplage.put("api", "%s/src/api/%s.js");
        frontTemplage.put("index", "%s/src/views/%s/index.vue");
        frontTemplage.put("header", "%s/src/views/%s/module/header.vue");
        frontTemplage.put("edit", "%s/src/views/%s/module/edit.vue");
        frontTemplage.put("eForm", "%s/src/views/%s/module/form.vue");
        
        for (Entry<String, String> entry : frontTemplage.entrySet())
        {
            Template template = engine.getTemplate("generator/front/" + entry.getKey() + ".ftl");
            String filePath = String.format(entry.getValue(), genConfig.getPath(), entityName);
            
            File file = new File(filePath);
            
            // 如果非覆盖生成
            if (!genConfig.getCover())
            {
                if (FileUtil.exist(file))
                {
                    continue;
                }
            }
            // 生成代码
            genFile(file, template, map);
        }
    }
    
    private void genFile(File file, Template template, Map<String, Object> map)
        throws IOException
    {
        // 生成目标文件
        Writer writer = null;
        try
        {
            FileUtil.touch(file);
            writer = new FileWriter(file);
            template.render(map, writer);
        }
        catch (TemplateException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            writer.close();
        }
    }
    
}
