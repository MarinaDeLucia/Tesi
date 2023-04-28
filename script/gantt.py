import argparse
import json
import matplotlib.pyplot as plt
from matplotlib.ticker import FormatStrFormatter, MaxNLocator

parser = argparse.ArgumentParser(description="Create Gantt charts for jobs and events in a scheduler")
parser.add_argument('-i', '--input', type=str, help="Input file name; JSON format, containing a list of events and jobs called 'events'")
parser.add_argument('-o', '--output', type=str, help='Output file name; it can be PNG, JPEG, PDF, SVG, EPS')
parser.add_argument('-t', '--title', type=str, help='Title to show on the chart')

args = parser.parse_args()

input_file = args.input
output_file = args.output
chart_title = args.title

with open(input_file, 'r') as file:
    data = json.load(file)
jobs=data['events']

# extract the relevant information into separate lists
machine_numbers = list({job['machine'] for job in jobs})
machine_numbers = [m for m in machine_numbers if m != 0] # remove machine 0

start_times = [job['start'] for job in jobs]
durations = [job['duration'] if job['type'] == 'JOB' else 0 for job in jobs]

job_names = [job['name'] for job in jobs if job['type'] == 'JOB']
unique_jobs = list(set(job_names))

event_names = [
    job['name']
    for job in jobs
    if job['type'] in ['EVENT', 'RESOURCE_LOAD', 'JOB_ARRIVAL']
]
unique_events = list(set(event_names))
event_payloads = [
    job['payload'].replace("\t", " " * 4)
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
unified_list = unique_jobs + unique_events
assigned_colors = [{'color': colors[list(colors.keys())[i % 10]], 'name': unified_list[i]} for i in range(len(unified_list))]

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


# plot the events as vertical lines
for i, event_name in enumerate(event_names):
    start = event_starts[i]
    ypos = len(machine_numbers)
    offset= i % len(machine_numbers)
    event_color=[d['color'] for d in assigned_colors if d['name'] == event_name]
    linestyle = '-' if event_types[i] == 'EVENT' else '-.' if event_types[i] == 'RESOURCE_LOAD' else ':'
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

# final formatting
ax.grid(False)
ax.invert_yaxis()
# Set the x-axis locator and formatter
ax.xaxis.set_major_locator(MaxNLocator(integer=True))
ax.xaxis.set_major_formatter(FormatStrFormatter('%d'))
fig.set_figwidth(15)
fig.set_figheight(10)

plt.title(chart_title, fontsize=24, fontweight='bold')
plt.savefig(output_file)
