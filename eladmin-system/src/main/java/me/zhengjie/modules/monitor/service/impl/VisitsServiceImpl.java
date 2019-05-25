package me.zhengjie.modules.monitor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import me.zhengjie.domain.Log;
import me.zhengjie.mapper.LogDao;
import me.zhengjie.modules.monitor.domain.Visits;
import me.zhengjie.modules.monitor.mapper.VisitsDao;
import me.zhengjie.modules.monitor.service.VisitsService;
import me.zhengjie.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author
 * @date 2018-12-13
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class VisitsServiceImpl implements VisitsService
{
    
    @Autowired
    private VisitsDao visitsDao;
    
    @Autowired
    private LogDao logDao;
    
    @Override
    public Visits save()
    {
        LocalDate localDate = LocalDate.now();
        Visits visits = visitsDao.selectOne(new QueryWrapper<Visits>().eq("date", localDate.toString()));
        if (visits == null)
        {
            visits = new Visits();
            visits.setWeekDay(StringUtils.getWeekDay());
            visits.setPvCounts(1L);
            visits.setIpCounts(1L);
            visits.setDate(localDate.toString());
            visitsDao.insert(visits);
        }
        return visits;
    }
    
    @Override
    public void count(HttpServletRequest request)
    {
        LocalDate localDate = LocalDate.now();
        Visits visits = visitsDao.selectOne(new QueryWrapper<Visits>().eq("date", localDate.toString()));
        if (null == visits)
        {
            visits = save();
        }
        visits.setPvCounts(visits.getPvCounts() + 1);
        Integer ipCounts = logDao.selectCount(
            new QueryWrapper<Log>().between("create_time", localDate.toString(), localDate.plusDays(1).toString())
                .groupBy("request_ip"));
        if (null != ipCounts)
        {
            visits.setIpCounts(ipCounts.longValue());
            visitsDao.updateById(visits);
        }
    }
    
    @Override
    public Map<String, Object> get()
    {
        Map<String, Object> map = new HashMap<String, Object>();
        LocalDate localDate = LocalDate.now();
        Visits visits = visitsDao.selectOne(new QueryWrapper<Visits>().eq("date", localDate.toString()));
        List<Visits> list = visitsDao.selectList(new QueryWrapper<Visits>()
            .between("create_time", localDate.minusDays(6).toString(), localDate.plusDays(1).toString()));
        
        long recentVisits = 0, recentIp = 0;
        for (Visits data : list)
        {
            recentVisits += data.getPvCounts();
            recentIp += data.getIpCounts();
        }
        map.put("newVisits", visits.getPvCounts());
        map.put("newIp", visits.getIpCounts());
        map.put("recentVisits", recentVisits);
        map.put("recentIp", recentIp);
        return map;
    }
    
    @Override
    public Map<String, Object> getChartData()
    {
        Map<String, Object> map = new HashMap<String, Object>();
        LocalDate localDate = LocalDate.now();
        List<Visits> list = visitsDao.selectList(new QueryWrapper<Visits>()
            .between("create_time", localDate.minusDays(6).toString(), localDate.plusDays(1).toString()));
        map.put("weekDays", list.stream().map(Visits::getWeekDay).collect(Collectors.toList()));
        map.put("visitsData", list.stream().map(Visits::getPvCounts).collect(Collectors.toList()));
        map.put("ipData", list.stream().map(Visits::getIpCounts).collect(Collectors.toList()));
        return map;
    }
}
