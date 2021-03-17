package org.alfresco.sdk.sample.filter;

import org.alfresco.event.sdk.handling.filter.AbstractEventFilter;
import org.alfresco.event.sdk.model.v1.model.DataAttributes;
import org.alfresco.event.sdk.model.v1.model.NodeResource;
import org.alfresco.event.sdk.model.v1.model.RepoEvent;
import org.alfresco.event.sdk.model.v1.model.Resource;

import java.util.List;
import java.util.Objects;

public class NodeMovedFromFolderFilter extends AbstractEventFilter {

    private final String parentId;

    private NodeMovedFromFolderFilter(final String parentId) {
        this.parentId = Objects.requireNonNull(parentId);
    }

    public static NodeMovedFromFolderFilter of(final String parentId) {
        return new NodeMovedFromFolderFilter(parentId);
    }

    @Override
    public boolean test(RepoEvent<DataAttributes<Resource>> event) {
        NodeResource resource = (NodeResource) event.getData().getResource();
        boolean parentFound = resource.getPrimaryHierarchy().get(0).equals(parentId);
        return isNodeEvent(event) && checkNodeMoved(event) && beforeFolderIsParent(event);
    }

    private boolean checkNodeMoved(final RepoEvent<DataAttributes<Resource>> event) {
        return hasResourceBefore(event) && checkPrimaryHierarchy(event);
    }

    private boolean checkPrimaryHierarchy(final RepoEvent<DataAttributes<Resource>> event) {
        final NodeResource nodeResourceBefore = (NodeResource) event.getData().getResourceBefore();
        final List<String> primaryHierarchy = nodeResourceBefore.getPrimaryHierarchy();
        return primaryHierarchy != null && !primaryHierarchy.isEmpty();
    }

    private boolean beforeFolderIsParent(final RepoEvent<DataAttributes<Resource>> event) {
        final NodeResource nodeResourceBefore = (NodeResource) event.getData().getResourceBefore();
        return nodeResourceBefore.getPrimaryHierarchy().get(0).equals(parentId);
    }
}
