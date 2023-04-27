import matplotlib.pyplot as plt
import json

with open('jobs.json', 'r') as file:
    data = json.load(file)
jobs=data['events']

# extract the relevant information into separate lists
machine_numbers = list({job['machine'] for job in jobs})
machine_numbers = [m for m in machine_numbers if m != 0] # remove machine 0

start_times = [job['start'] for job in jobs]
durations = [job['duration'] if job['type'] == 'JOB' else 0 for job in jobs]

job_names = [job['name'] for job in jobs if job['type'] == 'JOB']

event_names = [
    job['name']
    for job in jobs
    if job['type'] in ['EVENT', 'RESOURCE_LOAD', 'JOB_ARRIVAL']
]
event_payloads = [
    job['payload']
    for job in jobs
    if job['type'] in ['EVENT', 'RESOURCE_LOAD', 'JOB_ARRIVAL']
]
event_starts = [
    job['start']
    for job in jobs
    if job['type'] in ['EVENT', 'RESOURCE_LOAD', 'JOB_ARRIVAL']
]

event_types = [
    job['type']
    for job in jobs
    if job['type'] in ['EVENT', 'RESOURCE_LOAD', 'JOB_ARRIVAL']
]



# create a dictionary to map machine numbers to y-axis positions
machine_ypos = {machine_numbers[i]: i for i in range(len(machine_numbers))}

# create a dictionary of colors for events
colors = {'red': '#FF0000', 'green': '#00FF00', 'blue': '#0000FF', 'yellow': '#FFFF00', 'purple': '#800080', 'orange': '#FFA500', 'black': '#000000', 'gray': '#808080', 'pink': '#FFC0CB', 'brown': '#A52A2A'}

# assign a color to each job or event
assigned_colors = [colors[list(colors.keys())[i % 10]] for i in range(len(job_names)+len(event_names))]


# create the figure and axes
fig, ax = plt.subplots()

# set the x-axis labels and limits
ax.set_xlim([0, max(start_times)+max(durations)])

# set the y-axis labels and limits
ax.set_yticks(range(len(machine_numbers)))
ax.set_yticklabels(machine_numbers)
ax.set_ylim([-1, len(machine_numbers)])

color_index = 0

# plot the bars for jobs
for i, job_name in enumerate(job_names):
    if jobs[i]['type'] == 'JOB':
        start = start_times[i]
        duration = durations[i]
        machine = jobs[i]['machine']
        ypos = machine_ypos[machine]
        ax.barh(ypos, duration, left=start, height=0.5, align='center', color=assigned_colors[color_index], alpha=0.5)
        ax.text(start+duration/2, ypos, job_name, ha='center', va='center')
        color_index += 1

# plot the events as vertical lines
for i, event_name in enumerate(event_names):
    start = event_starts[i]
    ypos = len(machine_numbers)
    offset= i % len(machine_numbers)
    linestyle = '-' if event_types[i] == 'EVENT' else '-.' if event_types[i] == 'RESOURCE_LOAD' else ':'
    ax.plot([start, start], [-1, ypos], linewidth=1, color=assigned_colors[color_index], linestyle=linestyle)
    ax.annotate(f"{event_name}\n({event_payloads[i]})", xy=(start, (offset-1)+0.5), xytext=(start+1.2, (offset-1)+0.5),
            bbox=dict(boxstyle='round', facecolor='wheat', alpha=0.5),
            arrowprops=dict(arrowstyle='->', color='red'))
    ax.text(start, ypos, f"{event_name}", ha='center', va='bottom')
    color_index += 1

# Create a list of job names and their corresponding colors for the legend
legend_elements = [plt.Line2D([0], [0], color=assigned_colors[i], lw=2, label=job['name']) for i, job in enumerate(jobs) if job['type'] == 'JOB']
ax.legend(handles=legend_elements, loc='center left', bbox_to_anchor=(1, 0.5), fancybox=True, shadow=True)

# final formatting
ax.grid(False)
fig.set_figwidth(15)
fig.set_figheight(10)

plt.show()
