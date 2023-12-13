package io.github.ygojson.tools.dataprovider.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.ygojson.tools.dataprovider.utils.PaginatorStreamFactory.PageIterator;

class PaginatorStreamFactoryUnitTest {

	@Nested
	class PageIteratorTest {

		private static final PageIterator<String> EMPTY_TEST_ITERATOR =
			new PageIterator<>(any -> null);
		private static final PageIterator<String> INF_SINGLE_VALUE_TEST_ITERATOR =
			new PageIterator<>(any -> "value");

		private static final PageIterator<String> THROWING_ITERATOR =
			new PageIterator<>(any -> {
				throw new RuntimeException("Some error");
			});

		@Test
		void given_pageIterator_when_hasNext_then_returnFalse() {
			// given
			// when
			final boolean hasNext = EMPTY_TEST_ITERATOR.hasNext();
			// then
			assertThat(hasNext).isEqualTo(false);
		}

		@Test
		void given_pageIterator_when_next_then_throws() {
			// given
			// when
			final ThrowableAssert.ThrowingCallable callable =
				EMPTY_TEST_ITERATOR::next;
			// then
			assertThatThrownBy(callable).isInstanceOf(NoSuchElementException.class);
		}

		@Test
		void given_infiniteIterator_when_hasNext_then_returnTrue() {
			// given
			// when
			final boolean hasNext = INF_SINGLE_VALUE_TEST_ITERATOR.hasNext();
			// then
			assertThat(hasNext).isEqualTo(true);
		}

		@Test
		void given_infiniteIterator_when_next_then_returnValue() {
			// given
			// when
			final String nextPage = INF_SINGLE_VALUE_TEST_ITERATOR.next();
			// then
			assertThat(nextPage).isEqualTo("value");
		}

		@Test
		void given_throwingIterator_when_hasNext_then_returnFalse() {
			// given
			// when
			final boolean hasNext = THROWING_ITERATOR.hasNext();
			// then
			assertThat(hasNext).isEqualTo(false);
		}

		@Test
		void given_throwingIterator_when_next_then_throws() {
			// given
			// when
			final ThrowableAssert.ThrowingCallable callable = THROWING_ITERATOR::next;
			// then
			assertThatThrownBy(callable).isInstanceOf(NoSuchElementException.class);
		}

		static Stream<Arguments> listIteration() {
			return Stream.of(
				Arguments.of(List.of()),
				Arguments.of(List.of("one")),
				Arguments.of(List.of("first", "second")),
				Arguments.of(List.of("first", "second", "third"))
			);
		}

		@ParameterizedTest
		@MethodSource("listIteration")
		void given_queueIterator_when_iterateAll_then_correctIteration(
			final List<String> values
		) {
			// given
			final Queue<String> pagesQueue = new LinkedList<>(values);
			final Iterator<String> iterator = new PageIterator<>(next ->
				pagesQueue.poll()
			);
			// when
			final List<String> pages = new ArrayList<>();
			while (iterator.hasNext()) {
				pages.add(iterator.next());
			}
			// then
			assertThat(pages).isEqualTo(values);
		}

		@ParameterizedTest
		@MethodSource("listIteration")
		void given_queueIterator_when_iterateAllAndCallNext_then_throws(
			final List<String> values
		) {
			// given
			final Queue<String> pagesQueue = new LinkedList<>(values);
			final Iterator<String> iterator = new PageIterator<>(next ->
				pagesQueue.poll()
			);
			// when
			final List<String> pages = new ArrayList<>();
			while (iterator.hasNext()) {
				pages.add(iterator.next());
			}
			final ThrowableAssert.ThrowingCallable callable = iterator::next;
			// then
			assertThatThrownBy(callable).isInstanceOf(NoSuchElementException.class);
		}

		void given_exceptionHandler_when_iteratorFails_then_handlerProcessException() {}
	}

	@Test
	void given_sequentialNumberPageHandler_when_streamLimit_then_resultIsCorrect() {
		// given
		final Stream<Integer> asStream = PaginatorStreamFactory.create(prev -> {
			if (prev == null) {
				return 0;
			} else {
				return prev + 1;
			}
		});
		// when
		final List<Integer> actual = asStream.limit(10).toList();
		// then
		final List<Integer> expected = IntStream.range(0, 10).boxed().toList();
		assertThat(actual).isEqualTo(expected);
	}
}
