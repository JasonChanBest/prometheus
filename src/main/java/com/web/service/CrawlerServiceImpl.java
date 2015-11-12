package com.web.service;

import com.web.config.Config;
import com.web.crawler.Crawler;
import com.web.dao.CrawlerInfoDao;
import com.web.processor.AfterStartupProcessor;
import com.web.crawler.IDGenerator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author jayson   2015-08-19 17:08
 * @since v1.0
 */
@Service("CrawlerServiceImpl")
@Transactional
public class CrawlerServiceImpl implements CrawlerService , ApplicationContextAware , AfterStartupProcessor {
    @Autowired
    private CrawlerInfoDao dao;

    @Resource(name = "IDGenerator")
    private IDGenerator generator;

    @Resource(name = "Config")
    private Config config;

    private ApplicationContext context;

    private Map<Long , Crawler> crawlers = new HashMap<>();

    @Override
    public void afterStartup() {
        Map<String, Crawler> crawlerMap = context.getBeansOfType(Crawler.class, false, true);
        for(Entry<String , Crawler> e : crawlerMap.entrySet()){
            crawlers.put(e.getValue().getCrawlerInfo().getId() , e.getValue());
        }

        long maxId = maxId();
        if(maxId < config.getStartId())
            maxId = config.getStartId();

        generator.update(maxId);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public Collection<Crawler> crawlers() {
        return crawlers.values();
    }

    @Override
    public Crawler get(long id) {
        return crawlers.get(id);
    }

    @Override
    public long maxId() {
        return dao.maxId();
    }
}
