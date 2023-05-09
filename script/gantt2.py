import argparse
import json
import matplotlib.pyplot as plt
from matplotlib.ticker import FormatStrFormatter, MaxNLocator
import sys

parser = argparse.ArgumentParser(description="Create Gantt charts for jobs and events in a scheduler")
parser.add_argument('-i', '--input', type=str, help="Input file name; JSON format, containing a list of events and jobs called 'events'", required=True)
parser.add_argument('-o', '--output', type=str, help='Output file name; it can be PNG, JPEG, PDF, SVG, EPS')
parser.add_argument('-t', '--title', type=str, help='Title to show on the chart')
parser.add_argument('-s', '--show', help='Show the chart on a window', action='store_true')
parser.add_argument('-tx', '--ticks', help='Show all the ticks on X axis', action='store_true')

args = parser.parse_args()

input_file = args.input
output_file = args.output
chart_title = args.title
show_chart = args.show
ticks_x = args.ticks

if (output_file is None) and (show_chart is False):
    print('No argument for showing or saving the chart was chosen. Exiting the program...')
    sys.exit(1)

with open(input_file, 'r') as file:
    data = json.load(file)
jobs=data['events']
# sort the jobs primarily by 'start', and secondarily by 'name'
sorted_jobs = sorted(jobs, key=lambda x: (x['start'], x['name']))


# extract the relevant information into separate lists
machine_numbers = list({job['machine'] for job in jobs})
machine_numbers = [m for m in machine_numbers if m != 0] # remove machine 0

start_times = [job['start'] for job in jobs]
durations = [job['duration'] if job['type'] == 'JOB' else 0 for job in jobs]

sorted_start_times = [job['start'] for job in sorted_jobs]
sorted_durations = [job['duration'] if job['type'] == 'JOB' else 0 for job in sorted_jobs]

#add 'makespan' event
finish_times = [start_time + duration for start_time, duration in zip(sorted_start_times, sorted_durations)]
last_finish_time = max(finish_times)

# add makespan event
jobs = jobs + [{'name': f"makespan: {last_finish_time}", 'type': 'MARKER', 'machine': 0, 'start': last_finish_time, 'duration': 0, 'payload': ''}]
start_times = start_times + [last_finish_time]
durations = durations + [0]

# unique jobs list
job_names = [job['name'] for job in jobs if job['type'] == 'JOB']
unique_jobs=list(set(job_names))

# create a dictionary of lists with 'name' as key
jobs_by_name = {}
for d in jobs:
    jobs_by_name.setdefault(d['name'], []).append(d)

# find the dictionary with the smallest 'start' int value for each 'name'
fisrt_occurrences = []
for name, dicts in jobs_by_name.items():
    smallest_dict = min(dicts, key=lambda d: d['start'])
    fisrt_occurrences.append(smallest_dict)

#sort by start
result_list = sorted(fisrt_occurrences, key=lambda d: d['start'])
# keep just the names
sorted_names= [d['name'] for d in result_list]



event_names = [
    job['name']
    for job in jobs
    if job['type'] in ['EVENT', 'RESOURCE_LOAD', 'JOB_ARRIVAL', 'MARKER']
]

event_payloads = [
    job['payload'].replace("\t", " " * 4)
    for job in jobs
    if job['type'] in ['EVENT', 'RESOURCE_LOAD', 'JOB_ARRIVAL', 'MARKER']
]
event_starts = [
    job['start']
    for job in jobs
    if job['type'] in ['EVENT', 'RESOURCE_LOAD', 'JOB_ARRIVAL', 'MARKER']
]

event_types = [
    job['type']
    for job in jobs
    if job['type'] in ['EVENT', 'RESOURCE_LOAD', 'JOB_ARRIVAL', 'MARKER']
]

# create a dictionary to map machine numbers to y-axis positions
machine_ypos = {machine_numbers[i]: i for i in range(len(machine_numbers))}

# create a dictionary of colors for events
colors = {'red': '#FF0000', 'green': '#00FF00', 'blue': '#0000FF', 'yellow': '#FFFF00', 'purple': '#800080', 'orange': '#FFA500', 'black': '#000000', 'gray': '#808080', 'pink': '#FFC0CB', 'brown': '#A52A2A'}

# assign a color to each job or event
assigned_colors = [{'color': colors[list(colors.keys())[i % 10]], 'name': sorted_names[i]} for i in range(len(sorted_names))]

# create the figure and axes
fig, ax = plt.subplots()

# set the x-axis labels and limits
ax.set_xlim([0, max(start_times)+max(durations)])

# set the y-axis labels and limits
ax.set_yticks(range(len(machine_numbers)))
ax.set_yticklabels(machine_numbers)
ax.set_ylim([-1, len(machine_numbers)])


# plot the bars for jobs
for i, job_name in enumerate(job_names):
    if jobs[i]['type'] == 'JOB':
        start = start_times[i]
        duration = durations[i]
        machine = jobs[i]['machine']
        ypos = machine_ypos[machine]
        job_color=[d['color'] for d in assigned_colors if d['name'] == job_name]
        ax.barh(ypos, duration, left=start, height=0.5, align='center', color=job_color[0], alpha=0.5)
        ax.text(start+duration/2, ypos, job_name, ha='center', va='center')

ypos = len(machine_numbers)

# plot the events as vertical lines
for i, event_name in enumerate(event_names):
    start = event_starts[i]
    offset= i % ypos
    event_color=[d['color'] for d in assigned_colors if d['name'] == event_name]
    linestyle = '-' if event_types[i] == 'EVENT' else '-.' if event_types[i] == 'RESOURCE_LOAD' else ':' if event_types[i] == 'JOB_ARRIVAL' else '--'
    ax.plot([start, start], [-1, ypos], linewidth=1, color=event_color[0], linestyle=linestyle)
    payload="\n" + event_payloads[i] if event_payloads[i] !="" else ""
    ax.annotate(f"{event_name}{payload}", xy=(start, offset), xytext=(start+1.2, offset), fontfamily='monospace',
            bbox=dict(boxstyle='round', facecolor='wheat', alpha=0.5),
            arrowprops=dict(arrowstyle='->', color='red'))
    ax.text(start, ypos, f"{event_name}", ha='center', va='bottom')

# Create a list of job names and their corresponding colors for the legend
assigned_job_color=[(d['name'], d['color']) for d in assigned_colors if d['name'] in unique_jobs]

legend_elements = [plt.Line2D([0], [0], color=assigned_job_color[i][1], lw=2, label=assigned_job_color[i][0]) for i, job in enumerate(assigned_job_color)]
ax.legend(handles=legend_elements, loc='center left', bbox_to_anchor=(1, 0.5), fancybox=True, shadow=True)

if ticks_x:
    ax.set_xticks(range(min(start_times), last_finish_time+1, 1))

# final formatting
ax.grid(False)
ax.invert_yaxis()
# Set the x-axis locator and formatter
#ax.xaxis.set_major_locator(MaxNLocator(integer=True))
ax.xaxis.set_major_formatter(FormatStrFormatter('%d'))
fig.set_figwidth(15)
fig.set_figheight(10)

if chart_title != "":
    #plt.title(chart_title, fontsize=24, fontweight='bold', bbox=dict(facecolor='white', edgecolor='black', pad=1))
    plt.title(chart_title, fontsize=24, fontweight='bold')
if show_chart:
    plt.show()
if output_file:
    plt.savefig(output_file)
