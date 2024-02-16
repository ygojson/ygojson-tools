package io.github.ygojson.tools.dataprovider.domain;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.tools.dataprovider.PrintData;

public class PrintDataCollector {

	private AtomicInteger autoIncrement = new AtomicInteger(0);
	//private Map<Integer, PrintData> printDataByIds = new ConcurrentHashMap<>();
	private Map<Integer, InternalPrintData> printDataByIds = new ConcurrentHashMap<>();
	private Map<Set, List<Integer>> setToPrintDataMapping = new ConcurrentHashMap<>();
	private Map<Card, List<Integer>> cardToPrintDataMapping = new ConcurrentHashMap<>();



	private record InternalPrintData(int id, PrintData printData) {

		private InternalPrintData replaceSet(final Set newSet) {
			final PrintData newPrintData = new PrintData(printData.print(), printData.card(), newSet);
			printData.print().setSetId(newSet.getId());
			return new InternalPrintData(id, newPrintData);
		}

		private InternalPrintData replaceCard(final Card newCard) {
			final PrintData newPrintData = new PrintData(printData.print(), newCard, printData.set());
			printData.print().setCardId(newCard.getId());
			return new InternalPrintData(id, newPrintData);
		}

		private InternalPrintData replacePrint(final Print newPrint) {
			final PrintData newPrintData = new PrintData(newPrint, printData.card(), printData.set());
			return new InternalPrintData(id, newPrintData);
		}
	}

	public void addPrintData(final PrintData printData) {
		final int id = autoIncrement.incrementAndGet();
		printDataByIds.put(id, new InternalPrintData(id, printData));
		cardToPrintDataMapping.compute(printData.card(), (k, v) -> {
			if (v == null) {
				v = new ArrayList<>();
			}
			v.add(id);
			return v;
		});
		setToPrintDataMapping.compute(printData.set(), (k, v) -> {
			if (v == null) {
				v = new ArrayList<>();
			}
			v.add(id);
			return v;
		});
		/*
		final int id = autoIncrement.incrementAndGet();
		final InternalPrintData internalPrintData = new InternalPrintData(id, printData);
		printDataById.put(id, internalPrintData);
		setToPrintDataMapping.compute(printData.set(), (k, v) -> {
			if (v == null) {
				v = new ArrayList<>();
			}
			v.add(id);
			return v;
		});
		 */
	}

	public Stream<PrintData> getPrintData() {
		return printDataByIds.values().stream().map(InternalPrintData::printData);
	}

	public Stream<Set> getSets() {
		return setToPrintDataMapping.keySet().stream();
	}

	public boolean updatePrint(final Print print) {
		final Optional<InternalPrintData> matchingPrint = findPrintByCode(print.getPrintCode());
		if (matchingPrint.isEmpty()) {
			return false;
		}
		final InternalPrintData oldPrint = matchingPrint.get();
		if (print.equals(oldPrint.printData().print())) {
			return false;
		}
		final InternalPrintData newPrint = oldPrint.replacePrint(print);
		printDataByIds.put(newPrint.id(), newPrint);
		return true;
	}

	private Optional<InternalPrintData> findPrintByCode(String printCode) {
		return printDataByIds.values().stream()
			.filter(internalPrintData -> internalPrintData.printData().print().getPrintCode().equals(printCode))
			.findFirst();
	}

	public boolean updateCard(final Card card) {
		final Optional<Card> matchingCard = findCardByName(card.getName());
		if (matchingCard.isEmpty()) {
			// TODO: maybe throw or at least log
			return false;
		}
		final Card oldCard = matchingCard.get();
		if (oldCard.equals(card)) {
			return false;
		}
		final List<Integer> printDataIds = cardToPrintDataMapping.remove(oldCard);
		cardToPrintDataMapping.put(card, printDataIds);
		if (printDataIds != null && !printDataIds.isEmpty()) {
			printDataIds.forEach(id -> {
				final InternalPrintData oldPrintData = printDataByIds.remove(id);
				final InternalPrintData newPrintData = oldPrintData.replaceCard(card);
				printDataByIds.put(id, newPrintData);
			});
		}
		return true;
	}

	private Optional<Card> findCardByName(String name) {
		// TODO: should find by more than name
		return cardToPrintDataMapping.keySet().stream()
			.filter(card -> name.equals(card.getName()))
			.findFirst();
	}

	public boolean updateSet(final Set set) {
		final Optional<Set> matchingSet = findSetByName(set.getName());
		if (matchingSet.isEmpty()) {
			// TODO: maybe throw or at least log
			return false;
		}
		final Set oldSet = matchingSet.get();
		if (oldSet.equals(set)) {
			return false;
		}

		final List<Integer> printDataIds = setToPrintDataMapping.remove(oldSet);
		setToPrintDataMapping.put(set, printDataIds);
		if (printDataIds != null && !printDataIds.isEmpty()) {
			printDataIds.forEach(id -> {
				final InternalPrintData oldPrintData = printDataByIds.remove(id);
				final InternalPrintData newPrintData = oldPrintData.replaceSet(set);
				printDataByIds.put(id, newPrintData);
			});
		}
		return true;
	}

	private Optional<Set> findSetByName(final String name) {
		return setToPrintDataMapping.keySet().stream()
			.filter(set -> name.equals(set.getName()))
			.findFirst();
	}
}
