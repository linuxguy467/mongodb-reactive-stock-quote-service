package com.matthem.mongodbreactivestockquoteservice.service;

import com.matthem.mongodbreactivestockquoteservice.client.StockQuoteClient;
import com.matthem.mongodbreactivestockquoteservice.repositories.QuoteRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

@Service
public class QuoteMonitorService implements ApplicationListener<ContextRefreshedEvent> {
  private final StockQuoteClient stockQuoteClient;
  private final QuoteRepository quoteRepository;

  public QuoteMonitorService(StockQuoteClient stockQuoteClient, QuoteRepository quoteRepository) {
    this.stockQuoteClient = stockQuoteClient;
    this.quoteRepository = quoteRepository;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    stockQuoteClient.getQuoteStream()
        .log("quote-monitor-service")
        .subscribe(quote ->
            quoteRepository
                .save(quote)
                .subscribe(result ->
                    System.out.println("I saved a quote! Id: " + result.getId()))
        );
  }
}
