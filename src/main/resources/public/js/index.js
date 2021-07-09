$(document).ready(function () {
    // We must create Clipboard...
    new ClipboardJS('a');

    $('#short-url').submit(function (e) {
        e.preventDefault();

        const request = {
            url: $('#url').val()
        }

        $.ajax({
            url: '/api/short',
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(request),
            success: function (data) {
                // Create the card for the generated link
                let box = createURLDiv(data);
                // Hide for the moment
                $(box).hide();

                addURLToLocalStorage(data);

                // Add it with the rest of the URLs
                $('#my-links').prepend(box);

                // Perform the animation...
                $(box).slideDown()
            },
            error: function (data) {
                console.log(data);
                swal.fire('An error has occurred.', data.responseJSON.title);
            }
        });
    });

    // Event delegator for asking the API for a QR CODE.
    $(document).on('click', '.qr-code', function (e) {
        e.preventDefault();
        let code = $(this).attr('data-code-id');
        $.ajax({
            url: '/api/' + code + '/qr',
            type: 'get',
            dataType: 'json',
            success: function (data) {
                swal.fire({
                    title: 'QR Code',
                    text: data.redirectUrl,
                    imageUrl: data.imageBase64,
                    imageWidth: 500,
                    imageHeight: 500,
                    imageAlt: data.redirectUrl,
                });
            },
            error: function (data) {
                console.log(data);
                swal.fire('An error has occurred.', data.responseJSON.title);
            }
        })
    });

    function addURLToLocalStorage(data) {
        const urls = JSON.parse(localStorage.getItem('generated-links') || "[]");
        urls.push(JSON.stringify(data));
        localStorage.setItem('generated-links', JSON.stringify(urls));
    }

    function createURLDiv(data) {
        // Wrapper div
        let mainDiv = document.createElement('div');
        mainDiv.className = 'col-12 bg-white rounded-3 p-2 mb-5';

        // Container Div
        let containerDiv = document.createElement('div');
        containerDiv.className = 'container-fluid';

        // Row div
        let rowDiv = document.createElement('div');
        rowDiv.className = 'row justify-content-center align-items-center';

        // Div that contains the original link
        let originalUrlDiv = document.createElement('div');
        originalUrlDiv.className = 'col-12 col-md-6 col-lg-6 original-url fs-5 p-2';
        originalUrlDiv.innerText = data.originalUrl;

        // Div that contains the shorten url link
        let redirectUrlDiv = document.createElement('div');
        redirectUrlDiv.className = 'col-12 col-md-4 col-lg-4 shorten-url fs-5 p-2';

        let shortenUrl = document.createElement('a');
        shortenUrl.target = '_blank';
        shortenUrl.href = data.redirectUrl;
        shortenUrl.text = data.redirectUrl;
        redirectUrlDiv.appendChild(shortenUrl);

        // Prepare button for QR Code
        let divBtnQr = document.createElement('div');
        divBtnQr.className = 'col-12 col-md-1 col-lg-1 url-actions d-grid gap-2 p-2';
        let aBtnQr = document.createElement('a');
        aBtnQr.className = 'btn btn-sm btn-outline-dark b qr-code';
        $(aBtnQr).attr('data-code-id', data.code)
        let btnQr = document.createElement('i');
        btnQr.className = 'fa fa-qrcode fs-4';
        aBtnQr.appendChild(btnQr);
        divBtnQr.appendChild(aBtnQr);

        // Prepare Copy Button
        let divBtnCopy = document.createElement('div');
        divBtnCopy.className = 'col-12 col-md-1 col-lg-1 url-actions d-grid gap-2 p-2';
        let aBtnCopy = document.createElement('a');
        aBtnCopy.className = 'btn btn-sm btn-outline-dark b';
        $(aBtnCopy).attr('data-clipboard-text', data.redirectUrl);
        let btnCopy = document.createElement('i');
        btnCopy.className = 'fa fa-copy fs-4';
        aBtnCopy.appendChild(btnCopy);
        divBtnCopy.appendChild(aBtnCopy);


        rowDiv.appendChild(originalUrlDiv);
        rowDiv.appendChild(redirectUrlDiv);
        rowDiv.appendChild(divBtnQr);
        rowDiv.appendChild(divBtnCopy);

        containerDiv.appendChild(rowDiv);
        mainDiv.appendChild(containerDiv);
        return mainDiv;
    }
});