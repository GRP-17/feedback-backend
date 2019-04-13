package com.group17.util;

import org.springframework.hateoas.Link;

/**
 * The {@link org.springframework.hateoas.Link} utility class.
 */
public class LinkUtil {
	
	private LinkUtil() {}
	
	/**
	 * Trim any parameters from the end of a link.
	 * 
	 * @param link the {@link org.springframework.hateoas.Link} to process
	 * @return the resultant {@link org.springframework.hateoas.Link}
	 */
	public static Link removeParameters(Link link) {
		String ref = link.getHref();
		if(ref.contains("{?")) {
			return new Link(ref.substring(0, ref.indexOf('{')), link.getRel());
		} else if(ref.contains("{/")) {
			return new Link(ref.substring(0, ref.indexOf('{')), link.getRel());	
		} else {
			if(ref.contains("?")) {
				return new Link(ref.substring(0, ref.indexOf('?')), link.getRel());
			} else {
				return link;
			}
		}
	}

}
