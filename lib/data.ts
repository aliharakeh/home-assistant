// Mock data - in a real app, this would come from a database
export interface Shareholder {
    name: string
    amount: number
    isPercentage: boolean
}

export type RentDuration = 'monthly' | 'yearly'

export type SubscriptionType = 'main' | 'motor'

export interface ElectricityBill {
    id: string
    subscriptionType: SubscriptionType
    amount: number
    date: string // ISO date string
    notes?: string
}

export interface Home {
    name: string
    address: string
    tenant: string
    rent: number
    rentDuration: RentDuration
    electricityCode: string
    shareholders: Shareholder[]
    electricityBills: ElectricityBill[]
}

// Function to generate 12 bills for a home for the year 2025
function generateMonthlyBills(homeName: string, startId: number): ElectricityBill[] {
    const bills: ElectricityBill[] = []
    const currentYear = 2025
    for (let i = 0; i < 12; i++) {
        const month = (i + 1).toString().padStart(2, '0')
        const day = Math.floor(Math.random() * 28) + 1 // Random day between 1 and 28
        const date = `${currentYear}-${month}-${day.toString().padStart(2, '0')}`
        const mainAmount = Math.floor(Math.random() * 100) + 50 // Random amount between 50 and 150
        const motorAmount = Math.floor(Math.random() * 30) + 10 // Random amount between 10 and 40

        bills.push({
            id: `bill-${startId + i * 2}`,
            subscriptionType: 'main',
            amount: mainAmount,
            date: date,
            notes: i === 3 ? 'Checked for issues, all normal.' : undefined, // Example note
        })
        // Add motor bill every other month for variety, or always if preferred
        if (i % 2 === 0) {
            bills.push({
                id: `bill-${startId + i * 2 + 1}`,
                subscriptionType: 'motor',
                amount: motorAmount,
                date: date,
            })
        }
    }
    // Ensure exactly 12 bills, might need adjustment if motor bills are conditional
    // For simplicity, let's ensure we always have 12. If the above logic gives less, we add more main bills.
    let billIdCounter = startId + bills.length
    while (bills.length < 12) {
        const month = ((bills.length % 12) + 1).toString().padStart(2, '0') // Cycle through months if needed
        const day = Math.floor(Math.random() * 28) + 1
        const date = `${currentYear}-${month}-${day.toString().padStart(2, '0')}`
        const mainAmount = Math.floor(Math.random() * 100) + 50
        bills.push({
            id: `bill-${billIdCounter++}`,
            subscriptionType: 'main',
            amount: mainAmount,
            date: date,
        })
    }
    return bills.slice(0, 12) // Ensure exactly 12 bills
}

export const homes: Home[] = [
    {
        name: 'Sunset Villa',
        address: '123 Sunset Blvd, Los Angeles, CA 90001',
        tenant: 'John Smith',
        rent: 2500,
        rentDuration: 'monthly',
        electricityCode: 'EL-12345',
        shareholders: [
            { name: 'Jane Doe', amount: 50, isPercentage: true },
            { name: 'Mike Johnson', amount: 25, isPercentage: true },
        ],
        electricityBills: generateMonthlyBills('Sunset Villa', 1),
    },
    {
        name: 'Ocean View Apartment',
        address: '456 Beach Road, Miami, FL 33101',
        tenant: 'Sarah Williams',
        rent: 1800,
        rentDuration: 'monthly',
        electricityCode: 'EL-67890',
        shareholders: [{ name: 'Robert Brown', amount: 1000, isPercentage: false }],
        electricityBills: generateMonthlyBills('Ocean View Apartment', 13), // Start ID after previous home's bills
    },
    {
        name: 'Mountain Retreat',
        address: '789 Pine Street, Denver, CO 80201',
        tenant: 'Emily Davis',
        rent: 22000,
        rentDuration: 'yearly',
        electricityCode: 'EL-24680',
        shareholders: [],
        electricityBills: generateMonthlyBills('Mountain Retreat', 25), // Start ID after previous home's bills
    },
    {
        name: 'City Loft',
        address: '101 Main Street, New York, NY 10001',
        tenant: 'Michael Lee',
        rent: 3200,
        rentDuration: 'monthly',
        electricityCode: 'EL-11223',
        shareholders: [
            { name: 'Alice Green', amount: 60, isPercentage: true },
            { name: 'Bob White', amount: 40, isPercentage: true },
        ],
        electricityBills: generateMonthlyBills('City Loft', 37),
    },
    {
        name: 'Suburban House',
        address: '22 Oak Lane, Chicago, IL 60601',
        tenant: 'Jessica Brown',
        rent: 2800,
        rentDuration: 'monthly',
        electricityCode: 'EL-33445',
        shareholders: [{ name: 'David Black', amount: 100, isPercentage: true }],
        electricityBills: generateMonthlyBills('Suburban House', 49),
    },
    {
        name: 'Riverside Cottage',
        address: '55 River Road, Austin, TX 78701',
        tenant: 'Chris Wilson',
        rent: 15000,
        rentDuration: 'yearly',
        electricityCode: 'EL-55667',
        shareholders: [],
        electricityBills: generateMonthlyBills('Riverside Cottage', 61),
    },
]

export function getHomeByName(name: string): Home | undefined {
    return homes.find(home => home.name === name)
}

export function formatShareholderAmount(shareholder: Shareholder): string {
    if (shareholder.isPercentage) {
        return `${shareholder.amount}%`
    } else {
        return `$${shareholder.amount}`
    }
}

export function formatRent(rent: number, duration?: string): string {
    return duration ? `$${rent} / ${duration}` : `$${rent}`
}

export function formatDate(dateString: string): string {
    const date = new Date(dateString)
    return new Intl.DateTimeFormat('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
    }).format(date)
}

export function getSubscriptionTypeLabel(
    type: SubscriptionType,
    t?: (key: string) => string
): string {
    if (t) {
        return t(type) // Use translated string if translation function is provided
    }

    // Fallback to English if no translation function is provided
    switch (type) {
        case 'main':
            return 'Main'
        case 'motor':
            return 'Motor'
        default:
            return type
    }
}

export function calculateTotalBills(bills: ElectricityBill[]): {
    total: number
    main: number
    motor: number
} {
    return bills.reduce(
        (acc, bill) => {
            acc.total += bill.amount
            if (bill.subscriptionType === 'main') {
                acc.main += bill.amount
            } else if (bill.subscriptionType === 'motor') {
                acc.motor += bill.amount
            }
            return acc
        },
        { total: 0, main: 0, motor: 0 }
    )
}
