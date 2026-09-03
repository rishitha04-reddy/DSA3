# Function to calculate Edit Distance using Wagner-Fischer Algorithm
def edit_distance(s1, s2):
    # Find the length of both strings
    m = len(s1)
    n = len(s2)

    # Create a Dynamic Programming table
    dp = [[0 for j in range(n + 1)] for i in range(m + 1)]

    # Initialize the first column
    for i in range(m + 1):
        dp[i][0] = i

    # Initialize the first row
    for j in range(n + 1):
        dp[0][j] = j

    # Fill the Dynamic Programming table
    for i in range(1, m + 1):
        for j in range(1, n + 1):

            # If characters are the same
            if s1[i - 1] == s2[j - 1]:
                cost = 0
            else:
                # Replacement cost
                cost = 1

            # Minimum among deletion, insertion and replacement
            dp[i][j] = min(
                dp[i - 1][j] + 1,
                dp[i][j - 1] + 1,
                dp[i - 1][j - 1] + cost
            )

    return dp[m][n]


# Read words from the text file
with open("words.txt", "r") as file:
    words = file.read().splitlines()

# Take search word from the user
query = input("Enter search word: ")

print("\nSimilar Words")
print("----------------")

# Compare query with every word
for word in words:
    distance = edit_distance(query.lower(), word.lower())

    # Display similar words
    if distance <= 3:
        print(word, "Edit Distance =", distance)
