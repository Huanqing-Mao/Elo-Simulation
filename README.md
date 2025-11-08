# Elo Rating Simulation System

> A Java-based simulation system that models Elo rating changes for three types of participants (A, B, and C) answering questions together. The system uses a modified Elo rating algorithm to track how ratings evolve based on answer agreements and disagreements.

## Features

- **Three-Person Rating System**: Simulates rating changes when participants A, B, and C answer questions together
- **Modified Elo Algorithm**: Implements Elo rating with special logic for group dynamics
- **Statistical Analysis**: Generates percentile distributions and actual results for performance analysis
- **Flexible Simulation Parameters**: Configurable number of loops and questions per simulation
- **CSV Export**: Automatically exports results to CSV files for further analysis
- **Normal Distribution Generation**: Creates realistic participant pools with normally distributed accuracy levels

## Architecture

### Core Classes

#### `Person`
Represents a participant in the simulation with the following properties:
- **Type**: A, B, or C
- **ID**: Unique identifier
- **Rating**: Current Elo rating (defaults: A=1400, B=1400, C=1800)
- **Accuracy**: Probability of answering correctly (0.0 to 1.0)

**Key Methods**:
- `giveAnswer(int correctAnswer)`: Returns an answer based on the participant's accuracy
- `updateRating(double change)`: Updates the participant's Elo rating
- `resetRating(double rating)`: Resets rating to a specified value

#### `Rater`
Implements the Elo rating calculation and update logic for three-person groups.

**Key Constants**:
- `K = 40.0`: Elo rating change multiplier
- `CONSTANT = 400.0`: Elo rating scale constant
- `C_INSPECTION_RATE = 0.2`: Probability that C is inspected when A≠B

**Key Methods**:
- `calculateChange(Person p1, Person p2, double result)`: Calculates Elo rating change using standard formula
- `updateGroupRating(Person A, Person B, Person C, int Aanswer, int Banswer, int Canswer)`: Updates ratings based on answer comparisons with special logic:
  - When A = B: Direct comparison with C
  - When A ≠ B: C has 20% chance of being inspected; otherwise assumed correct
  - Handles edge cases like all correct, all wrong, and tie scenarios

#### `Simulator`
Main simulation engine that orchestrates the entire simulation process.

**Simulation Flow**:
1. Generates 11 C participants with accuracies from 0.2 to 1.0
2. For each C participant:
   - Runs `NUM_LOOP` independent experiments
   - Each experiment:
     - Generates 30 A and 30 B participants with normally distributed accuracies (mean=0.7, std=1.0, range=0.4-1.0)
     - Simulates `NUMQUESTIONS` questions with random A and B selection
     - Updates ratings after each question
3. Collects and aggregates results for statistical analysis

#### `Summariser`
Processes simulation results and exports data to CSV files.

**Key Methods**:
- `summariseC()`: Calculates percentiles for C participants
- `summariseAB()`: Calculates percentiles for A and B participants grouped by accuracy bins
- `writePercentileFile()`: Exports percentile distributions to CSV
- `writeResults()`: Exports actual rating results to CSV

## Installation & Compilation

### Prerequisites
- Java Development Kit (JDK) installed
- Command line access

### Compile Files
```bash
cd src
javac elosim/*.java
```

## Usage

### Running the Simulator
```bash
java elosim.Simulator
```

### Input Parameters
When prompted, enter two integers:
1. **NUM_LOOP**: Number of independent simulation runs (e.g., 1000)
2. **NUMQUESTIONS**: Number of questions per simulation run (e.g., 300)

**Example**:
```
1000 300
```

## Output Files

The simulator generates CSV files in the `data/` directory:

### Performance Distribution Files
Contains percentile statistics (5%, 20%, 30%, 50%, 70%, 90%, 95%) for each participant type:
- `A Performance Distribution {NUM_LOOP}loop {NUMQUESTIONS}qn.csv`
- `B Performance Distribution {NUM_LOOP}loop {NUMQUESTIONS}qn.csv`
- `C Performance Distribution {NUM_LOOP}loop {NUMQUESTIONS}qn.csv`

### Actual Results Files
Contains the actual final ratings for each simulation loop:
- `Actual Results C {NUM_LOOP}loop {NUMQUESTIONS}qn.csv`

**Note**: Actual results for A and B are commented out in the code due to performance considerations but can be enabled if needed.

## Key Parameters

### Default Settings
- **A Participants**: 30 per simulation, initial rating 1400
- **B Participants**: 30 per simulation, initial rating 1400
- **C Participants**: 11 total (accuracies: 0.2, 0.3, ..., 0.9, 0.95, 0.99, 1.0), initial rating 1800
- **A/B Accuracy Distribution**: Normal distribution (mean=0.7, std=1.0, range=0.4-1.0)
- **A/B Accuracy Bins**: 0.4-0.5, 0.5-0.6, 0.6-0.7, 0.7-0.8, 0.8-0.9, 0.9-1.0

### Rating System Parameters
- **K Factor**: 40.0
- **Rating Constant**: 400.0
- **C Inspection Rate**: 20% (when A≠B)
- **Reference Rating**: 1800 (used for comparisons when participants are below this threshold)

## Testing

The package includes test scripts for interactive testing:

- **testPerson.jsh**: Test Person class functionality
- **testRater.jsh**: Test Rater class with various scenarios

To use these tests, comment out the `package elosim;` line in the respective class files and run using Java Shell (jsh).

## Rating Logic Highlights

1. **When A = B = C**: No rating changes (all agree, no information)
2. **When A = B ≠ C**: Direct comparison between A/B and C
3. **When A ≠ B**: 
   - C has 20% chance of being inspected
   - If inspected: C's answer is used in rating calculations
   - If not inspected: C is assumed correct by default
4. **Rating Adjustments**: Participants below 1800 rating also compare against a reference 1800 rating

## Copyright

© huanqing.mao
