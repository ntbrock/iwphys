out = ""
for k in range(0,100):
	out += "step(a-" + str(k) + "*b)*step(" + str(k+1) + "*b-a)*(a-" + str(k) + "*b)+"
print out
