

FUZ_FACTOR = 2

RANGE = [40, 80, 120, 180, 300, 500, 900]


def hash(frequencies):
    hash,v = 0, 1
    for f in frequencies:
        hash += (f - (f % FUZ_FACTOR)) * v
        v *= 100
    return hash


def get_index(freq):
    i = 0
    while i < len(RANGE) and RANGE[i] < freq:
        i += 1
    return min(i, len(RANGE)-1)
