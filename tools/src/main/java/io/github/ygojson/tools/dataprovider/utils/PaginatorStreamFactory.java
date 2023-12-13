package io.github.ygojson.tools.dataprovider.utils;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Stream factory for paginated resources.
 * <br>
 * Note that in case of error on the {@link PageHandler#getNext(Object)}
 * the iteration is stop as finished. The exceptions can be handled
 * by implementing the {@link PageHandler#getNext(Object)}, otherwise
 * they will be logged as a warning.
 * <br>
 * Retry functionality is not an objective of this class and should be
 * handled on the {@link PageHandler#getNext(Object)} if necessary.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PaginatorStreamFactory {

	/**
	 * Creates a stream from a defined page handler.
	 * <br>
	 * Note that the assumption is that the handler returns an ordered
	 * iterato with unknown size and non-parallelizable.
	 *
	 * @param pageHandler handler controlling the page.
	 * @return initialized stream (first page is always loaded).
	 *
	 * @param <R> type for the page.
	 */
	public static <R> Stream<R> create(final PageHandler<R> pageHandler) {
		return StreamSupport.stream(
			Spliterators.spliteratorUnknownSize(
				new PageIterator<R>(pageHandler),
				Spliterator.ORDERED
			),
			false
		);
	}

	/**
	 * Handler providing the pages for the stream.
	 * <br>
	 * This object is a function that takes the previous
	 * page ({@code null} for the first one) and retunrs
	 * the next one (if any).
	 * <br>
	 * Note that no assumption is made on the next method,
	 * including unhandled exceptions. For that, a method
	 * can be implemented to handle it (by default, just logs).
	 *
	 * @param <T>
	 */
	@FunctionalInterface
	public interface PageHandler<T> {
		/**
		 * Gets the next page (if any).
		 * <br>
		 * Note that by contract even the first page can be {@code null},
		 * indicating an empty {@link PaginatorStreamFactory}.
		 *
		 * @param previous the previous page if any; {@code null} if it is the first page.
		 * @return the next page; {@code null} it the last page was reached.
		 *
		 * @throws Throwable to support unhandled exceptions.
		 */
		T getNext(final T previous) throws Throwable;

		/**
		 * Handle any error when calling the getter methods.
		 * <br>
		 * Default implementation ignores the error by logging it.
		 *
		 * @param e error thrown by any next request that was unhandled.
		 */
		default void handleError(final Throwable e) {
			log.error("Error requesting next page", e);
		}
	}

	@RequiredArgsConstructor
	static final class PageIterator<T> implements Iterator<T> {

		private final PageHandler<T> pageHandler;

		private Optional<T> currentPage;

		private Optional<T> requestNextPage(final T currentPage) {
			T nextPage;
			try {
				nextPage = pageHandler.getNext(currentPage);
			} catch (final Throwable e) {
				pageHandler.handleError(e);
				nextPage = null;
			}
			return Optional.ofNullable(nextPage);
		}

		@Override
		public boolean hasNext() {
			if (currentPage == null) {
				currentPage = requestNextPage(null);
			}
			if (currentPage.isEmpty()) {
				return false;
			}
			return true;
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException("No elements on the iterator");
			}
			// this is never null because otherwise hasNext will throw
			final T previousPage = currentPage.get();
			currentPage = requestNextPage(previousPage);
			return previousPage;
		}
	}
}
