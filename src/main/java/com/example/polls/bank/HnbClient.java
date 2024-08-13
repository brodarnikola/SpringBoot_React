package com.example.polls.bank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class HnbClient {

    private static final Logger LOG = LoggerFactory.getLogger(HnbClient.class);

    private static final String CURRENCY_QUERY_PARAM = "valuta";

    @Value("${hnb.base-url}")
    private String baseUrl;

    private RestClient restClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    /**
     *
     * Fetch list of HnbRateDto
     *
     * @return List<HnbRateDto>
     */
    public HnbRateDto[] getRates() {
        LOG.info("Get currencies");
        return restClient()
                .get()
                .retrieve()
                .onStatus(status -> status.value() == 404, (req, res) -> {
                    throw new HnbException("Unable to fetch rates from HNB");
                })
                .body(HnbRateDto[].class);
    }

    /**
     *
     * Fetch HnbRateDto object by currency parameter
     *
     * @param currency
     * @return HnbRateDto
     */
    public HnbRateDto getRate(final String currency) {
        LOG.info("Get currency: {}{}{}", baseUrl, "?valuta=", currency);
        final HnbRateDto[] rates = restClient()
                .get()
                .uri(uriBuilder -> uriBuilder.queryParam(CURRENCY_QUERY_PARAM, currency)
                        .build())
                .retrieve()
                .onStatus(status -> status.value() == 404, (req, res) -> {
                    throw new HnbException("Unable to fetch rate from HNB for currency: " + currency);
                })
                .body(HnbRateDto[].class);

        if (rates == null || rates.length < 1) {
            throw new HnbException("HNB rates list is null/empty");
        }

        return rates[0];
    }
}