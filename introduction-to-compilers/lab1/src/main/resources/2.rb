def func_y(x, n, c)
    result = (x**(1.to_f/4) - (1.to_f/x)**(1.to_f/4))**((-1)*n) + 
        (x + 1).to_f/(x**2 - 4*x + 3*n) + ((36*x*c*n)**(1.to_f/4))/((x + c*n + 1).to_f/(x+5))

    return result
end

def func_z(x, n, c)
    result = ((Math.tan(2*x)) * (Math.cos(2*x))**(-1) - (Math.tan(2*c + x)) * (Math.cos(2*x))**(-2)).to_f/((Math.cos(2*x))**(-1) + (Math.cos(3*x))**(-2)) +
        (1 + (Math.cos(n*x))**(1.to_f/c))/(2*x + (Math.sin(3*x))**2)

    return result
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


class Point
    def initialize(x, y)
        @x = x
        @y = y
    end

    def to_s
        return '(' + @x.to_s + '; ' + @y.to_s + ') '
    end

    def x
        return @x
    end

    def y
        return @y
    end
end

def tabulate_step(f, low, high, intervals_number, n, c)
    delta = (high - low).to_f / intervals_number
    result = []
    (low..high).step(delta){ |x|
        result.append(Point.new(x, method(f).call(x, n, c)))
    }
    return result
end

def tabulate_while(f, low, high, intervals_number, n, c)
    delta = (high - low).to_f / intervals_number
    result = []

    x = low
    while (x <= high)
        result.append(Point.new(x, method(f).call(x, n, c)))
        x += delta
    end

    return result
end 


def main
    n = 5
    c = 5
    pi = 3.141

    puts "1)", tabulate_step(:func_y, 1, n, n + c, n, c), "\n"
    puts "2)", tabulate_while(:func_z, pi/n, pi, (3.to_f/2)*n + c, n, c), "\n"
    puts "3)", tabulate_step(:func_f, 2, c, 2*n, n, c), "\n"
end

main