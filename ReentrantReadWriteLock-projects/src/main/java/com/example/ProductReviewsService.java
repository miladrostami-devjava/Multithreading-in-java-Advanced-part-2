package com.example;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ProductReviewsService {
    private final HashMap<Integer, List<String>> productIdToReviews;

    // Define read and write locks
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    /** Do not change this section **/

    public ProductReviewsService() {
        this.productIdToReviews = new HashMap<>();
    }

    /**
     * Add product ID if it doesn't exist
     */
    public void addProduct(int productId) {
        Lock lock = getLockForAddProduct();

        lock.lock();
        try {
            if (!productIdToReviews.containsKey(productId)) {
                productIdToReviews.put(productId, new ArrayList<>());
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Delete a product with a specific ID
     */
    public void removeProduct(int productId) {
        Lock lock = getLockForRemoveProduct();

        lock.lock();
        try {
            productIdToReviews.remove(productId);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Add a new comment for a product
     * @param productId - product ID (new or existing)
     * @param review - the text containing the product review
     */
    public void addProductReview(int productId, String review) {
        Lock lock = getLockForAddProductReview();

        lock.lock();
        try {
            productIdToReviews
                    .computeIfAbsent(productId, k -> new ArrayList<>())
                    .add(review);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returning all comments of a product
     */
    public List<String> getAllProductReviews(int productId) {
        Lock lock = getLockForGetAllProductReviews();

        lock.lock();
        try {
            return productIdToReviews.getOrDefault(productId, Collections.emptyList());
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returning the last comment of a product with a specified ID
     */
    public Optional<String> getLatestReview(int productId) {
        Lock lock = getLockForGetLatestReview();

        lock.lock();
        try {
            List<String> reviews = productIdToReviews.get(productId);
            if (reviews != null && !reviews.isEmpty()) {
                return Optional.of(reviews.get(reviews.size() - 1));
            }
        } finally {
            lock.unlock();
        }
        return Optional.empty();
    }

    /**
     * Returning all product IDs that have comments
     */
    public Set<Integer> getAllProductIdsWithReviews() {
        Lock lock = getLockForGetAllProductIdsWithReviews();

        lock.lock();
        try {
            Set<Integer> productsWithReviews = new HashSet<>();
            for (Map.Entry<Integer, List<String>> entry : productIdToReviews.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    productsWithReviews.add(entry.getKey());
                }
            }
            return productsWithReviews;
        } finally {
            lock.unlock();
        }
    }

    /** sections to add locks **/

    Lock getLockForAddProduct() {
        // We use the write lock to add the product
        return writeLock;
    }

    Lock getLockForRemoveProduct() {
        // We use the write lock to delete the product
        return writeLock;
    }

    Lock getLockForAddProductReview() {
        // We use the write lock to add a comment to the product
        return writeLock;
    }

    Lock getLockForGetAllProductReviews() {
        // We use a read lock to retrieve all comments
        return readLock;
    }

    Lock getLockForGetLatestReview() {
        // We use the read lock to retrieve the last comment
        return readLock;
    }

    Lock getLockForGetAllProductIdsWithReviews() {
        // We use the read lock to retrieve the IDs of the products with the comment
        return readLock;
    }
}