package org.apache.camel.opentracing.span;

import org.apache.camel.Category;
import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriPath;
import org.apache.camel.support.DefaultEndpoint;

/**
 * Tagging and adding/retrieving baggage items from the active OpenTracing Span.
 */
@UriEndpoint(firstVersion = "3.4.0", scheme = "opentracing-span", title = "OpenTracing Span",
        syntax = "opentracing-span:operation", producerOnly = true, category = {Category.MONITORING})
public class SpanEndpoint extends DefaultEndpoint {

    @UriPath(label = "producer")
    @Metadata(required = true)
    private SpanOperation operation;

    @UriPath(description = "The tag or baggage item key on the Span. For tag or setBaggage operations it's the key " +
            "that will be set on the Span, for getBaggage it's the key that will be used to retrieve from the Span.")
    private String spanKey;

    @UriPath(description = "The name of the Exchange message header where the the value will be retrieved from or " +
            "assigned to. For tag or setBaggage it's the name of the header whose value will be set on the Span, for " +
            "getBaggage it's the name of the header where the value retrieved from the Span will be assigned to.")
    private String valueHeader;

    public SpanEndpoint(String endpointUri, Component component) {
        super(endpointUri, component);
    }

    @Override
    public Producer createProducer() throws Exception {
        return new SpanProducer(this);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        throw new UnsupportedOperationException("Not supported");
    }

    public SpanOperation getOperation() {
        return operation;
    }

    /**
     * The operation to perform on the active Span.
     */
    public void setOperation(SpanOperation operation) {
        this.operation = operation;
    }

    public String getSpanKey() {
        return spanKey;
    }

    public void setSpanKey(String key) {
        this.spanKey = key;
    }

    public String getValueHeader() {
        return valueHeader;
    }

    public void setValueHeader(String valueHeader) {
        this.valueHeader = valueHeader;
    }
}
