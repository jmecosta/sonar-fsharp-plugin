module ComplexityDistributions

let GetFileComplexityDist(complexity : int) = 
    if complexity >= 0 && complexity <  5 then
        "0=1;5=0;10=0;20=0;30=0;60=0;90=0"
    elif complexity >= 5 && complexity <  10 then
        "0=0;5=1;10=0;20=0;30=0;60=0;90=0"
    elif complexity >= 10 && complexity <  20 then
        "0=0;5=0;10=1;20=0;30=0;60=0;90=0"
    elif complexity >= 20 && complexity <  30 then
        "0=0;5=0;10=0;20=1;30=0;60=0;90=0"
    elif complexity >= 30 && complexity <  60 then
        "0=0;5=0;10=0;20=0;30=1;60=0;90=0"
    elif complexity >= 60 && complexity <  90 then
        "0=0;5=0;10=0;20=0;30=0;60=1;90=0"
    else 
        "0=0;5=0;10=0;20=0;30=0;60=0;90=1"

let GetFunctionComplexityDist(complexities : int List) =
    let mutable range0 = 0
    let mutable range1 = 0
    let mutable range2 = 0
    let mutable range3 = 0
    let mutable range4 = 0
    let mutable range5 = 0
    let mutable range6 = 0

    let increaseRanges(complexity : int) =
        if complexity >= 1 && complexity < 2 then
            range0 <- range0 + 1
        elif complexity >= 2 && complexity <  4 then
            range1 <- range1 + 1
        elif complexity >= 4 && complexity <  6 then
            range2 <- range2 + 1
        elif complexity >= 6 && complexity <  8 then
            range3 <- range3 + 1
        elif complexity >= 8 && complexity <  10 then
            range4 <- range4 + 1
        elif complexity >= 10 && complexity <  12 then
            range5 <- range5 + 1
        else 
            range6 <- range6 + 1

    for complexity in complexities do
        increaseRanges(complexity)

    sprintf "1=%i;2=%i;4=%i;6=%i;8=%i;10=%i;12=%i" range0 range1 range2 range3 range4 range5 range6