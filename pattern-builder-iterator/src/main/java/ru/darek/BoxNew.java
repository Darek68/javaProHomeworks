package ru.darek;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class BoxNew {
    private final List<String> firstList ;
    private final List<String> secondList ;
    private final List<String> thirdList ;
    private final List<String> forthList ;
    private Iterator<String>[] itr ;

    public BoxNew(List<String> firstList, List<String> secondList, List<String> thirdList, List<String> forthList) {
        this.firstList = firstList;
        this.secondList = secondList;
        this.thirdList = thirdList;
        this.forthList = forthList;
        itr = new Iterator[]{firstList.iterator(), secondList.iterator(), thirdList.iterator(), forthList.iterator()};
    }

    public Iterator<String> getIterator(){
        return new BoxIterator(itr);
    }
    private class BoxIterator implements Iterator<String>  {
        private Iterator<java.lang.String>[] itr ;

        public BoxIterator(Iterator<java.lang.String>[] itr) {
            this.itr = itr;
        }
        @Override
        public String next() {
            for (int i = 0; i < 4; i++) {
                if (itr[i].hasNext()) return itr[i].next();
            }
            return null;
        }
        @Override
        public boolean hasNext() {
            for (int i = 0; i < 4; i++) {
                if (itr[i].hasNext()) return true;
            }
            return false;
        }
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}

