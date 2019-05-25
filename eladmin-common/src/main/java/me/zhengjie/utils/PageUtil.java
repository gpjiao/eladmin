package me.zhengjie.utils;

import org.springframework.data.domain.Page;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页工具
 * @author jie
 * @date 2018-12-10
 */
public class PageUtil extends cn.hutool.core.util.PageUtil {

    /**
     * List 分页
     * @param page
     * @param size
     * @param list
     * @return
     */
    public static List toPage(int page, int size , List list) {
        if (page >= 1)
        {
            page--;
        }
        int fromIndex = page * size;
        int toIndex = page * size + size;
        
        if (fromIndex > list.size())
        {
            return null;
        }
        else if (toIndex >= list.size())
        {
            return list.subList(fromIndex, list.size());
        }
        else
        {
            return list.subList(fromIndex, toIndex);
        }
    }
    
}
