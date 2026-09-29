# Session Walkthrough: Checking Antigravity Quota

This walkthrough documents the steps taken to check and understand the Antigravity session/model quota constraints.

## Goal
To determine the current quota and understand how to check/monitor model quota limits in the Google Antigravity ecosystem.

## Steps Executed

### 1. Located the `agy` CLI
We ran a command to check if the Antigravity CLI executable is installed on the host system:
```powershell
where.exe agy
```
**Output:**
```text
C:\Users\sahuk\AppData\Local\agy\bin\agy.exe
```

### 2. Evaluated CLI Flags and Capabilities
We inspected the available CLI subcommands:
```powershell
C:\Users\sahuk\AppData\Local\agy\bin\agy.exe --help
```
**Output Highlights:**
```text
Usage of agy.exe:
  --agent                         Agent for the current CLI session
  --model                         Model for the current CLI session
...
Available subcommands:
  agent           List available agents
  agents          List available agents
  changelog       Show changelog and release notes
  help            Show help for subcommands
  install         Configure environment paths and shell settings
  models          List available models
  plugin          Manage plugins
  update          Update CLI
```

### 3. Listed Available Models
We ran the `models` subcommand to check the model capabilities of the current session:
```powershell
C:\Users\sahuk\AppData\Local\agy\bin\agy.exe models
```
**Output:**
```text
Default model: gemini-3.5-flash-medium

Models available for this session:
  gemini-3.5-flash-medium
  gemini-3.5-pro-high
```

### 4. Tried CLI Quota Subcommand
We attempted to see if a CLI-based quota command existed:
```powershell
C:\Users\sahuk\AppData\Local\agy\bin\agy.exe quota
```
**Output:**
```text
agy.exe: error: unknown subcommand "quota"
```

### 5. Researched Quota Mechanisms
Through web searches and documentation retrieval, we identified the following methods to view and manage quotas:

1. **In-IDE / VS Code Extension Dashboard:**
   * **Antigravity Panel**: Search for "Antigravity Panel" in the Extension Marketplace (or via `Ctrl/Cmd + Shift + X`).
   * **Dashboard Views**: Provides visual pie charts of your usage by model family.
   * **Commands**: Use the VS Code Command Palette (`Ctrl/Cmd + Shift + P`) to trigger:
     * `Antigravity Panel: Open Panel`
     * `Antigravity Panel: Refresh Quota`
2. **Quota Structure:**
   * **5-Hour Refresh Cycle**: Tiers typically refresh standard limits every 5 hours.
   * **Weekly Cap**: In addition to the 5-hour cycle, there are weekly ceilings. Exceeding them can result in temporary lockouts.
   * **Compute Effort**: Quotas are consumed not just by message counts, but by "compute effort" (such as complex codebase lookups, browser automation steps, and reasoning/thinking tokens).
3. **Alternative Monitoring Extensions:**
   * **Antigravity Meta Vision**: A third-party tool that queries the language server directly to display real-time per-model usage.
