package org.alfresco.sdk.sample.filter;

import org.alfresco.event.sdk.handling.filter.AbstractEventFilter;
import org.alfresco.event.sdk.model.v1.model.DataAttributes;
import org.alfresco.event.sdk.model.v1.model.NodeResource;
import org.alfresco.event.sdk.model.v1.model.RepoEvent;
import org.alfresco.event.sdk.model.v1.model.Resource;

import java.util.Objects;

public class InHierarchyFilter extends AbstractEventFilter {

    private final String parentId;

    private InHierarchyFilter(final String parentId) {
        this.parentId = Objects.requireNonNull(parentId);
    }

    public static InHierarchyFilter of(final String parentId) {
        return new InHierarchyFilter(parentId);
    }

    @Override
    public boolean test(RepoEvent<DataAttributes<Resource>> event) {
        NodeResource resource = (NodeResource) event.getData().getResource();
        boolean parentFound = resource.getPrimaryHierarchy().contains(parentId);
        return isNodeEvent(event) && parentFound;
    }

}
