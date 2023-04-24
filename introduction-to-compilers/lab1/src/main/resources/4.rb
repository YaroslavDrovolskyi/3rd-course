def factorial(n)
    if (n < 0) then raise 'n can\'t be < 0' end
    n > 1 ? n * factorial(n - 1) : 1
end

def calculate_series_at_point(x, n = -1)
    if (n == -1) #calculate using eps
        eps = 1e-3
        sum = 1
        i = 1
        loop do
            addition = (((Math.log(3))**i)/(factorial(i))) * x**i
            if addition < eps
                return sum + addition
            else
                sum += addition
                i += 1
            end
        end
        return sum
    else
        sum = 0
        0.upto(n){|i|
            sum += (((Math.log(3))**i)/(factorial(i))) * x**i
        }
        return sum
    end
end

def calculate_series_in_interval(n = -1)
    if ((n < 10 || n > 58) && (n != -1)) then raise "n must be in [10; 58]" end
    result = []
    ((0.1)..1).step(0.1){|x|
        result.append(calculate_series_at_point(x, n))
    }
    return result
end

def main
    puts "\n\nWhen calculate using eps = 1e-3"
    puts calculate_series_in_interval()

    puts "\n\nWhen calculate sum up to 10-th series member"
    puts calculate_series_in_interval(10)

    puts "\n\nWhen calculate sum up to 20-th series member"
    puts calculate_series_in_interval(20)
end

main