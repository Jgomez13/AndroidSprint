#1. Binary search
#2. DEBFCA
#3 Stack
#4. O(lg n)

#1. since its already sorted in the highest "digit" place one would use a radix sort but just add the first chars to the result as the found order and follow from there

#---------------
#traverse through the list of 1 million once, while traversing add the first ten digits to a array since they are the smallest but in descending order (the first 100 integers). From then on only traverse through the million array 

#if the first in the smallest array (max of the smallest) is larger than the current number in the million array, then remove that old max and replace with cur from million array and insertion sort the already sorted smallest array. 

#if not then just move to the next integer in the million array (break in smallest array loop). 

#-----------
# assuming that there is a largest concat word of 2 words in list
#-----------
def longest_concat(words):
	words.sort(key=len, reverse =True) # sorted by length and in descending
	found = ""
	for i in range(len(words)):
		curr = words[i]
		matching = 0
		for j in range(len(words[i:])):
			if words[j] in curr:
				matching += 1 # found a word and update the count
		if matching > 1 : 
			found = curr  # only finding concat words made up of more than 1 other word. this works since we are going in descending order and only hacve to travers the list once since a word later in the list cant be made up of a word earlier in the list since the later word would be shorter than the earlier word.
			break
	print found
	return found
#----------------
def is_anagram(word,dictionary):
	s_w = sorted(word)
	for pos in dictionary:
		s_d =sorted(pos)
		if s_w == s_d:
			print "s_w: %s" % s_w
			print "s_d: %s" % s_d
			print "word:%s" % word
			print "pos: %s" % pos
			return True
	return False

words = ["dog", "cat", "bear", "dogcat", "elephant", "bearcat"]
word = "tac"
print "----------Longest concat---------"
longest_concat(words)
print "-----------is anagram------------"
is_anagram(word,words)
