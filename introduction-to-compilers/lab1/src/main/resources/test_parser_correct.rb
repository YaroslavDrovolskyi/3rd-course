def func_y(x, n, c)
    return x**n * c
end

def func_z(x, n, c)
    puts("hello from func_z")
    puts("hello")
    return z * n**c
end

class TestClass
    def reduce(x)
        until x < 5
            x /= 5
        end
        return x
    end

    def func_f(x, n, c)
        if (x > 2 && x < n)
            return func_y(x, n, c)
        elsif (x > n && x < 2*n)
            return func_z(x, n, c)
        else
            return func_y(x, n, c) + func_z(x, n, c)
        end
    end
end


def func_test()
    if (n == -1) #calculate using eps
        eps = 0.001
        sum = 1
        i = 1
        while x>5
            addition = (((Math.log(3))**i)/(factorial(i))) * x**i
            if addition < eps
                return sum + addition
            else
                sum += addition
                i += 1
            end
        end
        return sum
    end
end

def sum(a, b)
    return a + b
end

def main()
    a = 100; a = TestClass.reduce(a)
    b = 10
    return sum(a, b)
end

puts(main())