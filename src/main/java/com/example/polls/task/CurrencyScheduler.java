package com.example.polls.task;

import com.example.polls.bank.HnbClient;
import com.example.polls.bank.HnbRateDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@EnableScheduling
public class CurrencyScheduler {

//    private static final Logger LOG = LoggerFactory.getLogger(CurrencyScheduler.class);
//
//    private final HnbClient hnbClient;
//
//    public CurrencyScheduler(HnbClient hnbClient) {
//        this.hnbClient = hnbClient;
//    }
//
//    @Scheduled(cron = "0 0 12 * * 1-5") // start this scheduler every week day at 12
////    @Scheduled(cron = "*/5 * * * * *") // execute every 5 seconds cron job
//    public void startScheduler() {
//        LOG.info("Starting currency-scheduler");
//        this.getCurrencies();
//        LOG.info("Finished currency-scheduler");
//    }
//
//    private void getCurrencies() {
//        final CurrencyCache cache = CurrencyCache.getInstance();
//        final List<HnbRateDto> hnbRatesDtoList = Arrays.stream(hnbClient.getRates()).toList();
//
//        for (HnbRateDto rate : hnbRatesDtoList) {
//            LOG.info(formatRate(rate));
//        }
//
//        LOG.info("Currencies data size is: {}", hnbRatesDtoList.size());
//        LOG.info("Currencies data is: {}", hnbRatesDtoList);
//        hnbRatesDtoList.forEach(rate -> cache.getCurrencyMap().put(rate.getCurrency(), rate));
//    }
//
//    private String formatRate(HnbRateDto rate) {
//        return String.format(
//                "Rate Number: %s\nDate: %s\nCountry: %s\nCountry ISO: %s\nBuying Rate: %s\nMiddle Rate: %s\nSelling Rate: %s\nCurrency Code: %s\nCurrency: %s\n",
//                rate.getRateNumber(),
//                rate.getDate(),
//                rate.getCountry(),
//                rate.getCountryIso(),
//                rate.getBuyingRate(),
//                rate.getMiddleRate(),
//                rate.getSellingRate(),
//                rate.getCurrencyCode(),
//                rate.getCurrency()
//        );
//    }
}
