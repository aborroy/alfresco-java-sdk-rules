package org.alfresco.sdk.sample.filter;

import org.alfresco.event.sdk.handling.filter.AbstractEventFilter;
import org.alfresco.event.sdk.model.v1.model.DataAttributes;
import org.alfresco.event.sdk.model.v1.model.NodeResource;
import org.alfresco.event.sdk.model.v1.model.RepoEvent;
import org.alfresco.event.sdk.model.v1.model.Resource;

import java.util.Objects;

public class MimetypeFilter extends AbstractEventFilter {

    private final String mimetype;

    private MimetypeFilter(final String mimetype) {
        this.mimetype = Objects.requireNonNull(mimetype);
    }

    public static MimetypeFilter of(final String mimetype) {
        return new MimetypeFilter(mimetype);
    }

    @Override
    public boolean test(RepoEvent<DataAttributes<Resource>> event) {
        NodeResource resource = (NodeResource) event.getData().getResource();
        return isNodeEvent(event) && resource.getContent().getMimeType().equals(mimetype);
    }

}
