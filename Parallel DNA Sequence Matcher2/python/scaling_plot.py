import csv
import matplotlib.pyplot as plt

threads = []
speedups = []

with open('scaling.csv', newline='') as csvfile:
    reader = csv.DictReader(csvfile)
    for row in reader:
        threads.append(int(row['threads']))
        speedups.append(float(row['speedup']))

plt.figure(figsize=(8, 5))
plt.plot(threads, speedups, marker='o', linestyle='-', color='green')
plt.title('Speed-up vs. Number of Threads')
plt.xlabel('Threads')
plt.ylabel('Speed-up')
plt.grid(True)
plt.savefig('scaling_plot.png')
plt.show()