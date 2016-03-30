jQuery.validator.addMethod("dateRU", function(value, element) {
    if (!value == "") {
    var check = false;
    var re = /^\d{1,2}\.\d{1,2}\.\d{4}$/;
    var today = new Date();
    if (re.test(value)) {
        var adata = value.split('.');
        var gg = parseInt(adata[0], 10);
        var mm = parseInt(adata[1], 10);
        var aaaa = parseInt(adata[2], 10);
        var xdata = new Date(aaaa, mm - 1, gg);
        if (( xdata.getFullYear() == aaaa ) && ( xdata.getMonth() == mm - 1 ) && ( xdata.getDate() == gg ) &&
                (xdata.getFullYear() >= (today.getFullYear() - 100)) && (xdata <= today))
            check = true;
        else
            check = false;
    } else
        check = false;
    return this.optional(element) || check;
    }
    return true;
}, "Пожалуйста, введите корректную дату.");

$.validator.addMethod("lettersonly", function(value, element) {
    return this.optional(element) || /^[a-я\s]+$/i.test(value);
}, "Только цифры!");

jQuery.validator.addMethod("maxLandVal", function(value, element) {
    var check = true;
    check = parseInt(value) <= parseInt($("#benefitsOnHide").val());
    return this.optional(element) || check;
}, "Пожалуйста, введите значение меньшее максимального");

jQuery.validator.addMethod("maxContributeVal", function(value, element) {
    var check = true;
    check = parseInt(value) <= parseInt($("#memberBoardOnHide").val());
    return this.optional(element) || check;
}, "Пожалуйста, введите значение меньшее максимального");


