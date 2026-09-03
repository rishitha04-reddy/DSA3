# Evaluating Search Processing and Algorithm Selection

print("========== SEARCH ALGORITHM SELECTION ==========")

print("\n1. Small Unsorted Dataset")
print("2. Large Sorted Dataset")
print("3. Pattern Searching in Text")
print("4. Fast Pattern Searching")

choice = int(input("\nEnter your choice (1-4): "))

if choice == 1:
    print("\nRecommended Algorithm: Linear Search")
    print("Reason: Suitable for small and unsorted datasets.")
    print("Time Complexity: O(n)")

elif choice == 2:
    print("\nRecommended Algorithm: Binary Search")
    print("Reason: Efficient for large sorted datasets.")
    print("Time Complexity: O(log n)")

elif choice == 3:
    print("\nRecommended Algorithm: Naive Pattern Matching")
    print("Reason: Simple method for searching patterns in text.")
    print("Time Complexity: O(n * m)")

elif choice == 4:
    print("\nRecommended Algorithm: KMP Algorithm")
    print("Reason: Uses the LPS array to avoid repeated comparisons.")
    print("Time Complexity: O(n + m)")

else:
    print("\nInvalid Choice!")
