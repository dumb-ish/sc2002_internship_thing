# Internship Placement Management System

A comprehensive Java-based system for managing internship opportunities, applications, and placements. Built with SOLID principles and clean architecture.

## Table of Contents

- [Overview](#overview)
- [System Requirements](#system-requirements)
- [Quick Start](#quick-start)
- [Compilation & Execution](#compilation--execution)
- [Testing](#testing)
- [JavaDoc Documentation](#javadoc-documentation)

## Overview

The Internship Placement Management System facilitates the entire internship lifecycle from posting opportunities to final placements. It supports three user roles:
- **Students**: Browse and apply for internships
- **Company Representatives**: Post internships and manage applications
- **Career Center Staff**: Oversee and approve postings and representatives

## System Requirements

- **Java Development Kit (JDK)**: Version 8 or higher
- **Operating System**: Windows, macOS, or Linux
- **Memory**: Minimum 512 MB RAM
- **Disk Space**: 50 MB free space

## Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/dumb-ish/sc2002_internship_thing.git
cd sc2002_internship_thing
```

### 2. Compile the System

**Windows (PowerShell):**
```powershell
javac -encoding UTF-8 -source 8 -target 8 -d . *.java
```

**macOS/Linux (Terminal):**
```bash
javac -encoding UTF-8 -source 8 -target 8 -d . *.java
```

### 3. Run the System

**Windows (PowerShell):**
```powershell
java InternshipPlacementSystem
```

**macOS/Linux (Terminal):**
```bash
java InternshipPlacementSystem
```

## Compilation & Execution

### Compile All Classes

To compile all Java source files in the project:

```bash
javac -encoding UTF-8 -source 8 -target 8 -d . *.java
```

**Options explained:**
- `-encoding UTF-8` - Handle Unicode characters in source files
- `-source 8` - Accept Java 8 language features
- `-target 8` - Generate Java 8 compatible bytecode
- `-d .` - Places compiled `.class` files in the current directory

### Run the Application

After successful compilation, launch the system:

```bash
java InternshipPlacementSystem
```

### Compile and Run (One Command)

**Windows (PowerShell):**
```powershell
javac -encoding UTF-8 -source 8 -target 8 -d . *.java; if ($?) { java InternshipPlacementSystem }
```

**macOS/Linux (Bash):**
```bash
javac -encoding UTF-8 -source 8 -target 8 -d . *.java && java InternshipPlacementSystem
```

## Testing

The project includes comprehensive unit tests and black-box test cases aligned with AppendixA requirements.

### 📚 Test Documentation

- **[IMPLEMENTATION_SUMMARY.md](test/IMPLEMENTATION_SUMMARY.md)** ⭐ Start here - Complete testing overview
- **[APPENDIX_A_TEST_CASES.md](test/blackbox/APPENDIX_A_TEST_CASES.md)** ⭐ All 24 AppendixA test procedures
- **[Unit Test README](test/unit/README.md)** - Unit testing guide with implementation notes
- **[Test Plan](test/TEST_PLAN.md)** - Comprehensive test plan document
- **[Test Execution Guide](test/TEST_EXECUTION_GUIDE.md)** - Step-by-step execution instructions

### 🎯 Quick Start: AppendixA Testing (Recommended)

The fastest way to test all 24 AppendixA requirements:

1. **Compile and run the application:**
   ```powershell
   javac -encoding UTF-8 -source 8 -target 8 -d . *.java
   java InternshipPlacementSystem
   ```

2. **Open the black-box test script:**
   - File: [test/blackbox/APPENDIX_A_TEST_CASES.md](test/blackbox/APPENDIX_A_TEST_CASES.md)
   - Contains all 24 test cases with step-by-step procedures

3. **Execute tests manually:**
   - Follow procedures for TC001 through TC024
   - Mark checkboxes (☐ PASS / ☐ FAIL)
   - Test data includes: U2310001A, STAFF001, hr@techcorp.com (all use password: "password")

### Test Structure

```
test/
├── IMPLEMENTATION_SUMMARY.md        # Complete overview of testing framework ⭐
├── TEST_PLAN.md                    # Comprehensive test plan
├── TEST_EXECUTION_GUIDE.md         # Execution instructions
├── TEST_RESULTS_TEMPLATE.md        # Results recording template
├── blackbox/
│   └── APPENDIX_A_TEST_CASES.md    # All 24 AppendixA test procedures ⭐
├── unit/
│   ├── README.md                   # Unit testing guide
│   ├── AuthenticationTest.java     # 8 authentication tests
│   ├── ApplicationTest.java        # 8 application tests
│   ├── InternshipTest.java         # 6 internship tests
│   └── FilterTest.java             # 8 filter/sort tests
└── resources/
    ├── README.md                   # Test data documentation
    ├── test_students.csv           # 8 test students
    ├── test_staff.csv              # 3 staff members
    ├── test_company_representatives.csv  # 5 company reps
    └── test_internships.csv        # 8 internships
```

### Quick Test Setup

1. **Download JUnit libraries:**

**Windows:**
```powershell
New-Item -ItemType Directory -Force -Path lib
Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/junit/junit/4.13.2/junit-4.13.2.jar" -OutFile "lib\junit-4.13.2.jar"
Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar" -OutFile "lib\hamcrest-core-1.3.jar"
```

**macOS/Linux:**
```bash
mkdir -p lib
curl -L "https://repo1.maven.org/maven2/junit/junit/4.13.2/junit-4.13.2.jar" -o "lib/junit-4.13.2.jar"
curl -L "https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar" -o "lib/hamcrest-core-1.3.jar"
```

2. **Compile unit tests:**

**Windows:**
```powershell
javac -encoding UTF-8 -cp ".;lib\junit-4.13.2.jar;lib\hamcrest-core-1.3.jar" -d . test\unit\*.java
```

**macOS/Linux:**
```bash
javac -encoding UTF-8 -cp ".:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar" -d . test/unit/*.java
```

3. **Run all unit tests:**

**Windows:**
```powershell
java -cp ".;lib\junit-4.13.2.jar;lib\hamcrest-core-1.3.jar" org.junit.runner.JUnitCore AuthenticationTest ApplicationTest InternshipTest FilterTest
```

**macOS/Linux:**
```bash
java -cp ".:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore AuthenticationTest ApplicationTest InternshipTest FilterTest
```

### Test Categories

#### Unit Tests
- **Authentication Tests** (UT-AUTH-001 to UT-AUTH-008)
  - Login validation
  - Registration processes
  - Credential verification

- **Application Tests** (UT-APP-001 to UT-APP-008)
  - Application submission
  - Status updates
  - Withdrawal handling

- **Internship Tests** (UT-INT-001 to UT-INT-006)
  - Posting creation
  - Approval workflows
  - Data validation

- **Filter & Sort Tests** (UT-FILTER-001 to UT-SORT-002)
  - Location/salary/company filtering
  - Ascending/descending sorting

#### Black-Box Tests
- **Student Workflows** (BB-STU-001 to BB-STU-004)
- **Company Representative Workflows** (BB-REP-001 to BB-REP-004)
- **Career Center Staff Workflows** (BB-STAFF-001 to BB-STAFF-004)
- **Edge Cases & Error Handling** (BB-EDGE-001 to BB-EDGE-007)

### Running Black-Box Tests

Black-box tests are performed manually by following the detailed test scripts in the [Test Execution Guide](test/TEST_EXECUTION_GUIDE.md).

1. Start the application:
   ```bash
   java InternshipPlacementSystem
   ```

2. Follow test case steps in TEST_EXECUTION_GUIDE.md
3. Record results (✅ PASS / ❌ FAIL)

## JavaDoc Documentation

### Generate JavaDoc

To generate comprehensive HTML documentation:

**Windows (PowerShell):**
```powershell
javadoc -d docs -sourcepath . -private -author -version -use -windowtitle "Internship Placement Management System" -doctitle "Internship Placement Management System API Documentation" -header "SC2002 Group Project" *.java
```

**macOS/Linux (Terminal):**
```bash
javadoc -d docs -sourcepath . -private -author -version -use \
  -windowtitle "Internship Placement Management System" \
  -doctitle "Internship Placement Management System API Documentation" \
  -header "SC2002 Group Project" \
  *.java
```

**JavaDoc Options:**
- `-d docs` - Output directory for generated documentation
- `-private` - Include private members in documentation
- `-author` - Include @author tags
- `-version` - Include @version tags
- `-use` - Create class and package usage pages

### Access JavaDoc

After generation, open the documentation:

**Windows:**
```powershell
Start-Process docs\index.html
```

**macOS:**
```bash
open docs/index.html
```

**Linux:**
```bash
xdg-open docs/index.html
```

Or manually navigate to: `docs/index.html` in your web browser.

**Key Documentation Pages:**
- `index.html` - Main documentation homepage
- `allclasses-index.html` - Complete class listing
- `overview-tree.html` - Class hierarchy visualization
- `index-all.html` - Comprehensive alphabetical index

