package org.apache.camel.opentracing.span;

import io.opentracing.Span;
import org.apache.camel.AsyncCallback;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.opentracing.ActiveSpanManager;
import org.apache.camel.support.DefaultAsyncProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Span producer.
 */
public class SpanProducer extends DefaultAsyncProducer {
    private static final Logger LOG = LoggerFactory.getLogger(SpanProducer.class);

    public SpanProducer(Endpoint endpoint) {
        super(endpoint);
    }

    @Override
    public SpanEndpoint getEndpoint() {
        return (SpanEndpoint) super.getEndpoint();
    }

    @Override
    public boolean process(Exchange exchange, AsyncCallback callback) {
        try {
            Span span = ActiveSpanManager.getSpan(exchange);
            if (span != null) {
                switch (getEndpoint().getOperation()) {
                    case tag:
                        span.setTag(getEndpoint().getSpanKey(), exchange.getMessage().getHeader(getEndpoint().getValueHeader(), String.class));
                        break;
                    case setBaggage:
                        span.setBaggageItem(getEndpoint().getSpanKey(), exchange.getMessage().getHeader(getEndpoint().getValueHeader(), String.class));
                        break;
                    case getBaggage:
                        exchange.getMessage().setHeader(getEndpoint().getValueHeader(), span.getBaggageItem(getEndpoint().getSpanKey()));
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported operation");
                }

            } else {
                LOG.warn("OpenTracing: could not find managed span for exchange={}", exchange);
            }
        } catch (Exception e) {
            exchange.setException(e);
        } finally {
            // callback must be invoked
            callback.done(true);
        }

        return true;
    }
}
