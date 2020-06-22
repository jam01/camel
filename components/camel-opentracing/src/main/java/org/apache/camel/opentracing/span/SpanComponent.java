package org.apache.camel.opentracing.span;

import org.apache.camel.Endpoint;
import org.apache.camel.spi.annotations.Component;
import org.apache.camel.support.DefaultComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * For interacting with the active OpenTracing Span.
 */
@Component("opentracing-span")
public class SpanComponent extends DefaultComponent {

    private static final Logger LOG = LoggerFactory.getLogger(SpanComponent.class);

    public SpanComponent() {
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        if (remaining == null || remaining.trim().length() == 0) {
            throw new IllegalArgumentException("Operation must be specified.");
        }

        SpanEndpoint answer = new SpanEndpoint(uri, this);
        answer.setOperation(SpanOperation.valueOf(remaining));
        setProperties(answer, parameters);

        return answer;
    }
}
