class TestClass
    def func_test_loop()
        until x > 5
            a = 5
            b = 5*5**5
        end
        return a+b
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
        eps = 1e-3
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


def func_y(x, n, c)
    result = (x**(1.to_f/4) - (1.to_f/x)**(1.to_f/4))**((-1)*n)


#            result = (x**(1.to_f/4) - (1.to_f/x)**(1.to_f/4))**((-1)*n) +
#        (x + 1).to_f/(x**2 - 4*x + 3*n) + ((36*x*c*n)**(1.to_f/4))/((x + c*n + 1).to_f/(x+5))

    return result
end

def func_z(x, n, c)
    result = ((Math.tan(2*x)) * (Math.cos(2*x))**(-1) - (Math.tan(2*c + x)) * (Math.cos(2*x))**(-2)).to_f/((Math.cos(2*x))**(-1) + (Math.cos(3*x))**(-2)) + (1 + (Math.cos(n*x))**(1.to_f/c))/(2*x + (Math.sin(3*x))**2)

    return result
end

