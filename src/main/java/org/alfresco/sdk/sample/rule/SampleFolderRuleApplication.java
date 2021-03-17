package org.alfresco.sdk.sample.rule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.alfresco.core.handler.NodesApi;
import org.alfresco.core.model.Node;
import org.alfresco.core.model.NodeBodyUpdate;
import org.alfresco.event.sdk.handling.filter.EventTypeFilter;
import org.alfresco.event.sdk.integration.EventChannels;
import org.alfresco.event.sdk.integration.filter.IntegrationEventFilter;
import org.alfresco.event.sdk.model.v1.model.DataAttributes;
import org.alfresco.event.sdk.model.v1.model.NodeResource;
import org.alfresco.event.sdk.model.v1.model.RepoEvent;
import org.alfresco.event.sdk.model.v1.model.Resource;
import org.alfresco.sdk.sample.filter.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootApplication
public class SampleFolderRuleApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SampleFolderRuleApplication.class);
    @Autowired
    NodesApi nodesApi;
    @Autowired
    private ObjectMapper objectMapper;

    public static void main(final String[] args) {
        SpringApplication.run(SampleFolderRuleApplication.class, args);
    }

    /**
     * Event: Items are created or enter this folder
     */
    @Bean
    public IntegrationFlow ruleCreated() {

        Node node = nodesApi.getNode("-root-", null, "Shared/resources", null).getBody().getEntry();

        String folderId = node.getId();
        LOGGER.info("Folder Id to be monitored is {}", folderId);

        return IntegrationFlows.from(EventChannels.MAIN)
                .filter(IntegrationEventFilter.of(EventTypeFilter.NODE_CREATED.and(ParentFolderFilter.of(folderId))))
                .handle(t -> LOGGER.info("Node Created - Folder - Event: {}", t.getPayload().toString()))
                .get();

    }

    /**
     * Event: Items are updated
     */
    @Bean
    public IntegrationFlow ruleUpdated() {

        Node node = nodesApi.getNode("-root-", null, "Shared/resources", null).getBody().getEntry();

        String folderId = node.getId();
        LOGGER.info("Folder Id to be monitored is {}", folderId);

        return IntegrationFlows.from(EventChannels.MAIN)
                .filter(IntegrationEventFilter.of(EventTypeFilter.NODE_UPDATED.and(ParentFolderFilter.of(folderId))))
                .handle(t -> LOGGER.info("Node Updated - Folder - Event: {}", t.getPayload().toString()))
                .get();

    }

    /**
     * Event: Items are deleted or leave this folder
     */
    @Bean
    public IntegrationFlow ruleDeletedOrMoved() {

        Node node = nodesApi.getNode("-root-", null, "Shared/resources", null).getBody().getEntry();

        String folderId = node.getId();
        LOGGER.info("Folder Id to be monitored is {}", folderId);

        return IntegrationFlows.from(EventChannels.MAIN)
                .filter(IntegrationEventFilter.of(
                        EventTypeFilter.NODE_DELETED
                                .and(ParentFolderFilter.of(folderId))
                                .or(NodeMovedFromFolderFilter.of(folderId))))
                .handle(t -> LOGGER.info("Node Deleted or Moved - Folder - Event: {}", t.getPayload().toString()))
                .get();

    }

    /**
     * Event: Items are created or enter this folder
     * Option: Rule applies to subfolders
     */
    @Bean
    public IntegrationFlow ruleCreatedSubfolders() {

        Node node = nodesApi.getNode("-root-", null, "Shared/resources", null).getBody().getEntry();

        String folderId = node.getId();
        LOGGER.info("Folder Id to be monitored is {}", folderId);

        return IntegrationFlows.from(EventChannels.MAIN)
                .filter(IntegrationEventFilter.of(EventTypeFilter.NODE_CREATED.and(InHierarchyFilter.of(folderId))))
                .handle(t -> LOGGER.info("Node Created - Hierarchy - Event: {}", t.getPayload().toString()))
                .get();

    }

    /**
     * Event: Items are created or enter this folder
     * Condition: If all criteria are met (mimetype is pdf)
     */
    @Bean
    public IntegrationFlow ruleCreatedMimetypePDF() {

        Node node = nodesApi.getNode("-root-", null, "Shared/resources", null).getBody().getEntry();

        String folderId = node.getId();
        LOGGER.info("Folder Id to be monitored is {}", folderId);

        return IntegrationFlows.from(EventChannels.MAIN)
                .filter(IntegrationEventFilter.of(EventTypeFilter.NODE_CREATED
                        .and(ParentFolderFilter.of(folderId))
                        .and(MimetypeFilter.of("application/pdf"))))
                .handle(t -> LOGGER.info("Node Created Mimetype PDF - Folder - Event: {}", t.getPayload().toString()))
                .get();

    }

    /**
     * Event: Items are created or enter this folder
     * Condition: If all criteria are met (name begins with a)
     */
    @Bean
    public IntegrationFlow ruleCreatedNameBeginsWithA() {

        Node node = nodesApi.getNode("-root-", null, "Shared/resources", null).getBody().getEntry();

        String folderId = node.getId();
        LOGGER.info("Folder Id to be monitored is {}", folderId);

        return IntegrationFlows.from(EventChannels.MAIN)
                .filter(IntegrationEventFilter.of(EventTypeFilter.NODE_CREATED
                        .and(ParentFolderFilter.of(folderId))
                        .and(NameWildcardFilter.of("a", NameWildcardFilter.Match.BEGINS_WITH))))
                .handle(t -> LOGGER.info("Node Created Name begins with a - Folder - Event: {}", t.getPayload().toString()))
                .get();

    }

    /**
     * Event: Items are created or enter this folder
     * Condition: If all criteria are met (mimetype is pdf)
     * Action: Add aspect Index Control
     */
    @Bean
    public IntegrationFlow ruleCreatedMimetypePDFAddAspect() {

        Node node = nodesApi.getNode("-root-", null, "Shared/resources", null).getBody().getEntry();

        String folderId = node.getId();
        LOGGER.info("Folder Id to be monitored is {}", folderId);

        return IntegrationFlows.from(EventChannels.MAIN)
                .filter(IntegrationEventFilter.of(EventTypeFilter.NODE_CREATED
                        .and(ParentFolderFilter.of(folderId))
                        .and(MimetypeFilter.of("application/pdf"))))
                .handle(t -> {

                    LOGGER.info("Node Created Mimetype PDF Action - Folder - Event: {}", t.getPayload().toString());

                    RepoEvent<DataAttributes<Resource>> repoEvent = (RepoEvent<DataAttributes<Resource>>) t.getPayload();
                    NodeResource resource = (NodeResource) repoEvent.getData().getResource();

                    String nodeId = resource.getId();
                    Set<String> aspects = resource.getAspectNames();
                    aspects.add("cm:indexControl");

                    nodesApi.updateNode(nodeId,
                            new NodeBodyUpdate()
                                    .aspectNames(List.copyOf(aspects))
                                    .properties(Map.of(
                                            "cm:isIndexed", "false",
                                            "cm:isContentIndexed", "false")),
                            null, null);

                })
                .get();

    }

    @PostConstruct
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

}
