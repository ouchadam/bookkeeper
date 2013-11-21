package com.example;

import android.view.View;

class ItemManipulator {

    private final ChildFetcher childFetcher;

    public ItemManipulator(ChildFetcher childFetcher) {
        this.childFetcher = childFetcher;
    }

    public ExampleListAdapter.ViewHolder getItemViewHolder(int itemIdPosition) throws ViewHolderNotFoundException {
        View root = childFetcher.getChildAt(itemIdPosition);
        if (root != null && root.findViewById(R.id.item_root) != null) {
            View viewHolderRoot = root.findViewById(R.id.item_root);
            if (root.getTag() != null && root.getTag() instanceof ExampleListAdapter.ViewHolder) {
                return (ExampleListAdapter.ViewHolder) viewHolderRoot.getTag();
            }
        }
        throw new ViewHolderNotFoundException("ViewHolder was not found, prepare the backup plan");
    }

    static class ViewHolderNotFoundException extends Exception {
        ViewHolderNotFoundException(String detailMessage) {
            super(detailMessage);
        }
    }

}
