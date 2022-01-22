package com.msxall.marmsx.util;

import com.msxall.marmsx.AppIOException;

import javax.swing.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class ResourceLoader {
	public static Optional<URL> getAbsoluteResourceAsURL(String resourceName) {
		return getRelativeResourceAsURL(ResourceLoader.class, resourceName);
	}

	public static Optional<URL> getRelativeResourceAsURL(Class<?> clazz, String resourceName) {
		return Optional.ofNullable(clazz.getResource(resourceName));
	}

	public static Path getAbsoluteResourceAsPath(String resourceName) {
		return getRelativeResourceAsPath(ResourceLoader.class, resourceName);
	}

	public static Path getRelativeResourceAsPath(Class<?> clazz, String resourceName) {
		Optional<URL> optionalURL = getRelativeResourceAsURL(clazz, resourceName);

		if (optionalURL.isEmpty()) {
			throw new AppIOException("Resource not found: " + resourceName);
		}

		try {
			URI resourceURI = optionalURL.get().toURI();
			return Paths.get(resourceURI);
		} catch (URISyntaxException e) {
			throw new AppIOException("Invalid URI for: " + resourceName);
		}
	}

	public static ImageIcon loadImageIconFromAbsoluteResource(String resourceName) {
		Optional<URL> optionalURL = getAbsoluteResourceAsURL(resourceName);
		return optionalURL.map(ImageIcon::new).orElseThrow(AppIOException::new);
	}
}
