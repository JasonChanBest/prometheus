package com.web.crawler;

import cn.edu.hfut.dmic.webcollector.crawler.BreadthCrawler;
import com.web.entity.CrawlerInfoEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @author jayson   2015-08-11 16:34
 * @since v1.0
 */
public abstract class Crawler extends BreadthCrawler {
    private static final Logger LOGGER = LoggerFactory.getLogger(Crawler.class);
    protected CrawlerInfoEntity crawlerInfo;

    /**
     * @param crawlPath 维护URL信息的文件夹，如果爬虫需要断点爬取，每次请选择相同的crawlPath
     * @param autoParse 是否自动抽取符合正则的链接并加入后续任务
     */
    public Crawler(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
    }
    public Crawler() {
        this(null, true);
    }

    public void start(){
        try {
            for(String seed : crawlerInfo.getSeeds())
                addSeed(seed);

            for(String regex : crawlerInfo.getRegex())
                addRegex(regex);

            setTopN(crawlerInfo.getTopN());
            setAutoParse(crawlerInfo.isAutoParse());

            Class<? extends Crawler> clazz = this.getClass();
            Class<?> superClazz = null;
            while (true){
                superClazz = clazz.getSuperclass();
                if(superClazz == cn.edu.hfut.dmic.webcollector.crawler.Crawler.class){
                    break;
                }
            }
            Field field = superClazz.getDeclaredField("crawlPath");
            field.setAccessible(true);
            field.set(this , getCrawlPath());

            setThreads(Runtime.getRuntime().availableProcessors());
            setResumable(crawlerInfo.isResumable());
            setMaxRetry(crawlerInfo.getMaxRetry());
            setRetry(crawlerInfo.getRetry());

            super.start(crawlerInfo.getDepth());
        } catch (Exception e) {
            LOGGER.error("" , e);
        }
    }

    public CrawlerStatus getStatus(){
        return status == RUNNING ? CrawlerStatus.Running : CrawlerStatus.Stop;
    }

    public abstract String getCrawlPath();

    /**getter、setter方法**/
    public CrawlerInfoEntity getCrawlerInfo() {
        return crawlerInfo;
    }

    public void setCrawlerInfo(CrawlerInfoEntity crawlerInfo) {
        this.crawlerInfo = crawlerInfo;
    }
}