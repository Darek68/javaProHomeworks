package ru.darek;

import java.util.*;

public final class BoxNew {
    private final List<String> firstList = new ArrayList<>();
    private final List<String> secondList = new ArrayList<>();
    private final List<String> thirdList = new ArrayList<>();
    private final List<String> forthList = new ArrayList<>();

    public BoxNew(List<String> firstList, List<String> secondList, List<String> thirdList, List<String> forthList) {
        this.firstList.addAll(firstList == null ? Collections.emptyList() : firstList);
        this.secondList.addAll(secondList == null ? Collections.emptyList() : secondList);
        this.thirdList.addAll(thirdList == null ? Collections.emptyList() : thirdList);
        this.forthList.addAll(forthList == null ? Collections.emptyList() : forthList);
    }

    public Iterator<String> getIterator() {
        return new BoxIterator();
    }

    private class BoxIterator implements Iterator<String> {
        private Iterator<String>[] itr;

        public BoxIterator() {
            this.itr = new Iterator[]{firstList.iterator(), secondList.iterator(), thirdList.iterator(), forthList.iterator()};
        }

        @Override
        public String next() {
            for (int i = 0; i < 4; i++) {
                if (itr[i].hasNext()) return itr[i].next();
            }
            throw new IllegalStateException("Empty");
        }
        @Override
        public boolean hasNext() {
            for (int i = 0; i < 4; i++) {
                if (itr[i].hasNext()) return true;
            }
            return false;
        }
    }
}

