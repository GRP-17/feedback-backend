package com.group17.util;

public class RelBuilder {
	private StringBuilder building;
	
	private RelBuilder(LinkType type) {
		this.building = new StringBuilder(type.getPath().toString());
	}
	
	public RelBuilder withPrefix(String prefix) {
		StringBuilder prefixBuilder = new StringBuilder(prefix);

		// This will make it so we have rels such as:
		// - feedback 			(ROOT)
		// - feedback_count		(COUNT)
		// (Assuming feedback is the prefix)
		if(building.length() > 0) {
			prefixBuilder.append("_");
		}

		building = prefixBuilder.append(building);
		return this;
	}
	
	public String build() {
		return building.toString();
	}
	
	public static RelBuilder newInstance(LinkType linkType) {
		return new RelBuilder(linkType);
	}
	
	public static enum LinkType {
		ROOT(""),
		FINDALL("findall"),
		COUNT("count"),
		SENTIMENT_COUNT("sentiment_count"),
		RATING_AVERAGE("rating_average"),
		RATING_COUNT("rating_count");
		
		private final String path;
		
		private LinkType(String path) {
			this.path = path;
		}
		
		public String getPath() {
			return path;
		}
	}

}
