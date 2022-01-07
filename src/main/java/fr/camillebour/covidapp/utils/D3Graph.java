package fr.camillebour.covidapp.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.camillebour.covidapp.models.User;

import java.util.ArrayList;
import java.util.List;

public class D3Graph {

    private List<D3Node> nodes;
    private List<D3Link> links;

    public D3Graph() {
        this.nodes = new ArrayList<D3Node>();
        this.links = new ArrayList<D3Link>();
    }

    public static D3Graph fromUsers(List<User> users) {
        D3Graph graph = new D3Graph();
        users.forEach(u -> {
                    graph.nodes.add(new D3Node(u.getId(), u.getFullName(), u.isPositiveToCovid() ? 1 : 0));
                    u.getFriends().forEach(
                            fr -> graph.links.add(new D3Link(u.getId(), fr.getId()))
                    );
                }
        );
        return graph;
    }

    public static class D3Node {
        private Long id;
        private String name;
        private int colorGroup;

        D3Node(Long id, String name, int colorGroup) {
            this.id = id;
            this.name = name;
            this.colorGroup = colorGroup;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getColorGroup() {
            return colorGroup;
        }

        public void setColorGroup(int colorGroup) {
            this.colorGroup = colorGroup;
        }
    }

    public static class D3Link {
        @JsonProperty("source")
        private Long sourceId;

        @JsonProperty("target")
        private Long targetId;

        D3Link(Long sourceId, Long targetId) {
            this.sourceId = sourceId;
            this.targetId = targetId;
        }

        public Long getSourceId() {
            return sourceId;
        }

        public void setSourceId(Long sourceId) {
            this.sourceId = sourceId;
        }

        public Long getTargetId() {
            return targetId;
        }

        public void setTargetId(Long targetId) {
            this.targetId = targetId;
        }
    }
}

