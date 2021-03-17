package org.alfresco.sdk.sample.filter;

import org.alfresco.event.sdk.handling.filter.AbstractEventFilter;
import org.alfresco.event.sdk.model.v1.model.DataAttributes;
import org.alfresco.event.sdk.model.v1.model.NodeResource;
import org.alfresco.event.sdk.model.v1.model.RepoEvent;
import org.alfresco.event.sdk.model.v1.model.Resource;

import java.util.Objects;

public class NameWildcardFilter extends AbstractEventFilter {

    private final String name;
    private final Match match;

    public enum Match {BEGINS_WITH, CONTAINS, ENDS_WITH, EQUALS}

    private NameWildcardFilter(final String name, final Match match) {
        this.name = Objects.requireNonNull(name);
        this.match = Objects.requireNonNull(match);
    }

    public static NameWildcardFilter of(final String name, final Match match) {
        return new NameWildcardFilter(name, match);
    }

    @Override
    public boolean test(RepoEvent<DataAttributes<Resource>> event) {
        NodeResource resource = (NodeResource) event.getData().getResource();
        boolean matched = false;
        switch (match) {
            case BEGINS_WITH: matched = resource.getName().startsWith(name); break;
            case CONTAINS: matched = resource.getName().contains(name); break;
            case ENDS_WITH: matched = resource.getName().endsWith(name); break;
            case EQUALS: matched = resource.getName().equals(name); break;
        }
        return isNodeEvent(event) && matched;
    }

}
