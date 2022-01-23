package com.msxall.marmsx.util;

import com.msxall.marmsx.exception.AppIOException;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class ResourceLoader {


	private static final int MAX_STRING_READ_LENGTH = 32 * 1024 * 1024;

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
			System.out.println(">>>" + resourceURI);
			return Paths.get(resourceURI);
		} catch (URISyntaxException e) {
			throw new AppIOException("Invalid URI for: " + resourceName);
		}
	}

	public static ImageIcon loadImageIconFromAbsoluteResource(String resourceName) {
		Optional<URL> optionalURL = getAbsoluteResourceAsURL(resourceName);
		return optionalURL.map(ImageIcon::new).orElseThrow(AppIOException::new);
	}

	public static InputStream getAbsoluteResourceAsSteam(String resourceName) {
		return getRelativeResourceAsSteam(ResourceLoader.class, resourceName);
	}

	public static InputStream getRelativeResourceAsSteam(Class<?> clazz, String resourceName) {
		try {
			final InputStream resourceAsStream = clazz.getResourceAsStream(resourceName);

			if (resourceAsStream == null) {
				throw new AppIOException("Resource not found: " + resourceName);
			}

			return resourceAsStream;
		} catch (NullPointerException e) {
			throw new AppIOException("Resource not found: " + resourceName);
		}
	}

	public static String getAbsoluteResourceAsString(String resourceName) {
		return getRelativeResourceAsString(ResourceLoader.class, resourceName);
	}

	public static String getRelativeResourceAsString(Class<?> clazz, String resourceName) {
		try(final InputStream relativeResourceAsSteam = getRelativeResourceAsSteam(clazz, resourceName)) {
			return new String(relativeResourceAsSteam.readNBytes(MAX_STRING_READ_LENGTH));
		} catch (IOException | FileSystemNotFoundException e) {
			throw new AppIOException("IO Exception while loading resource: " + resourceName, e);
		}
	}
}
