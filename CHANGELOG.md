# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).
This project does NOT adhere to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.14.0] - Unreleased

### Added
- New navigation system inspired by jetpack compose
- Support for different inventory types like Anvil, and the ability to swap between them. See anvil gui example.
- InventoryHolder system which manages switching between different inventories and manages viewers
- Added some composables we created under MineInAbyss/MineInAbyss (ex. PlayerHead)
- Back handler that pops navigation stack, or exits when empty by default

### Changed
- `onClose` doesn't require a `reopen` call anymore, instead reopening on its own. Call `exit` explicitly to exit (the default behaviour.)
- Updates some composables to use nicer callbacks, ex. Paginated/Scrollable
- Players are now passed once on gui creation, not per Inventory screen
- Major internal cleanup and refactoring, unfortunately this leads to some package names changing

### Fixed
- Flickering GUIs when switching screens
- Mouse cursor resetting when switching screens of the same height
- Use an immediate-style Dispatcher to let compose correctly finish recompositions in one loop
- Double clicks being sent as well as two single click events with the default `clickable` modifier. This lead to unexpected extra clicks by default which are now fixed.
