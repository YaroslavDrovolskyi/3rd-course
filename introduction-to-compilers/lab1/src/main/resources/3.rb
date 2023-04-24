def get_repeated_entries_count(array)
    if (array.length == 0) then raise "Array must be not empty" end
    max = array.max
    min = array.min

    entries_count = Array.new(max - min + 1, 0)
    
    array.each{ |x|
        entries_count[x - min] += 1
    }

    repeated_numbers_count = 0
    entries_count.each{ |x|
        if x > 1 then repeated_numbers_count += 1 end
    }

    return repeated_numbers_count
end

def display_result(array)
    print "array  =  "
    print array
    print "\nresult = ", get_repeated_entries_count(array), "\n\n"
end

def main
    display_result([1, 2, 2, 3])
    display_result([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12])
    display_result([1, 2, 1, 2, 3, 4, 5, 1, 3, 6, 5, 8])
    display_result([1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 0])
    display_result([5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5])
    display_result([1, 2, -1, 2, 3, -4, 5, 1, 3, 6, -5, 8])
end


main