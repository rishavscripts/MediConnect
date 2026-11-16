# Design System & UI Guidelines — Medi-Connect

## 1. Design vision

Medi-Connect should feel calm, trustworthy, clear, and approachable. The interface should help users complete healthcare-administration tasks without visual clutter or confusing medical claims.

The visual direction is a modern healthcare product using a restrained blue/teal palette, neutral backgrounds, readable typography, and clear status indicators.

## 2. Design principles

1. **Clarity first:** use plain language and clear labels.
2. **Trust:** show data sources and distinguish verified data from user-entered information.
3. **Safety:** make urgent-care guidance and limitations visible where relevant.
4. **Accessibility:** support keyboard navigation, screen readers, and sufficient contrast.
5. **Consistency:** reuse spacing, typography, controls, and interaction patterns.
6. **Responsive:** design for mobile, tablet, and desktop.

## 3. Suggested visual tokens

These are starting values. Review contrast and validate them in the actual UI before finalizing.

### Colors

| Token | Suggested value | Usage |
|---|---|---|
| Primary | `#2563EB` | Primary actions and links |
| Primary dark | `#1D4ED8` | Hover/active |
| Teal | `#0F766E` | Secondary healthcare accent |
| Background | `#F8FAFC` | Main page background |
| Surface | `#FFFFFF` | Cards and panels |
| Text | `#0F172A` | Main text |
| Muted text | `#64748B` | Secondary text |
| Border | `#E2E8F0` | Dividers and outlines |
| Success | `#15803D` | Successful status |
| Warning | `#B45309` | Caution status |
| Danger | `#B91C1C` | Error/urgent warning |

Do not rely on color alone to communicate status. Pair colors with text or icons.

## 4. Typography

- Use a highly legible sans-serif font.
- Suggested choices: Inter, system UI, or a comparable accessible font.
- Use a clear hierarchy: page title, section title, card title, body, helper text.
- Avoid very small text, especially for medication schedules and important warnings.
- Use sentence case for labels and buttons.

## 5. Layout

### Public pages
- Simple top navigation.
- Clear product value proposition.
- Short explanations of the three core modules.
- Visible Sign Up and Login actions.
- About, Contact, privacy, and terms links.

### Auth pages
- Focused form layout.
- Clear field labels and validation messages.
- Password visibility control if implemented accessibly.
- Links to login/register and password recovery.

### Dashboard
- Greeting and concise overview.
- Cards for:
  - Medication Assistance
  - Hospital & Doctor Search
  - Personal Medication Assistant
- Upcoming schedule and appointment summaries.
- Recent activity only where useful.
- Avoid displaying sensitive details unnecessarily on shared screens.

### Module pages
- Consistent page header and breadcrumb where helpful.
- Clear primary action.
- Filters separated from results.
- Empty, loading, and error states.
- Confirm destructive actions.

## 6. Components

Build reusable components for:
- Button (primary, secondary, outline, danger).
- Text input, select, textarea, date/time picker.
- Form field with label, hint, and error.
- Card and summary card.
- Modal/dialog.
- Toast/alert.
- Badge/status indicator.
- Tabs.
- Pagination.
- Loading skeleton/spinner.
- Empty state.
- Confirmation dialog.
- Navigation bar, sidebar, and footer.

Use semantic HTML and accessible component patterns.

## 7. Interaction rules

- Buttons must have clear action labels.
- Disable submit buttons while a request is in progress.
- Preserve user input after recoverable errors.
- Provide confirmation for destructive actions.
- Use inline validation close to the relevant field.
- Do not use motion as the only way to communicate a state.
- Respect reduced-motion preferences.
- Do not show a success state until the backend confirms the operation.

## 8. Healthcare-specific presentation

- Clearly label medication information as general reference information.
- Show source and last-updated/retrieved information when available.
- Distinguish user-entered prescription details from verified reference data.
- Never style generated suggestions as confirmed diagnoses or prescriptions.
- Make allergy and emergency warnings clear without creating unnecessary alarm.
- Avoid misleading progress indicators that imply clinical improvement.

## 9. Accessibility checklist

- All inputs have programmatic labels.
- All controls are keyboard accessible.
- Focus indicators are visible.
- Color contrast meets WCAG AA where applicable.
- Images have meaningful alternative text or are marked decorative.
- Errors are announced to assistive technology.
- Tables have appropriate headers.
- Layout remains usable at zoom and on narrow screens.

## 10. Responsive behavior

- Mobile: single-column content, compact navigation, touch-friendly controls.
- Tablet: flexible two-column layouts where appropriate.
- Desktop: sidebar plus content area for authenticated dashboards.
- Avoid fixed widths that cause horizontal scrolling.

## 11. Design acceptance criteria

A page is ready when it is visually consistent, responsive, keyboard-usable, has loading/empty/error states, and presents health-related information with appropriate context and warnings.
