package edu.pucmm.eict.grpc.generated;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.39.0)",
    comments = "Source: ShortUrlProto.proto")
public final class ShortUrlServiceRnGrpc {

  private ShortUrlServiceRnGrpc() {}

  public static final String SERVICE_NAME = "edu.pucmm.eict.grpc.generated.ShortUrlServiceRn";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<edu.pucmm.eict.grpc.generated.ShortUrlProto.RequestPageShortUrlWithUser,
      edu.pucmm.eict.grpc.generated.ShortUrlProto.PageShortUrlResponse> getGetShortUrlsByUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getShortUrlsByUser",
      requestType = edu.pucmm.eict.grpc.generated.ShortUrlProto.RequestPageShortUrlWithUser.class,
      responseType = edu.pucmm.eict.grpc.generated.ShortUrlProto.PageShortUrlResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.pucmm.eict.grpc.generated.ShortUrlProto.RequestPageShortUrlWithUser,
      edu.pucmm.eict.grpc.generated.ShortUrlProto.PageShortUrlResponse> getGetShortUrlsByUserMethod() {
    io.grpc.MethodDescriptor<edu.pucmm.eict.grpc.generated.ShortUrlProto.RequestPageShortUrlWithUser, edu.pucmm.eict.grpc.generated.ShortUrlProto.PageShortUrlResponse> getGetShortUrlsByUserMethod;
    if ((getGetShortUrlsByUserMethod = ShortUrlServiceRnGrpc.getGetShortUrlsByUserMethod) == null) {
      synchronized (ShortUrlServiceRnGrpc.class) {
        if ((getGetShortUrlsByUserMethod = ShortUrlServiceRnGrpc.getGetShortUrlsByUserMethod) == null) {
          ShortUrlServiceRnGrpc.getGetShortUrlsByUserMethod = getGetShortUrlsByUserMethod =
              io.grpc.MethodDescriptor.<edu.pucmm.eict.grpc.generated.ShortUrlProto.RequestPageShortUrlWithUser, edu.pucmm.eict.grpc.generated.ShortUrlProto.PageShortUrlResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getShortUrlsByUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.pucmm.eict.grpc.generated.ShortUrlProto.RequestPageShortUrlWithUser.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.pucmm.eict.grpc.generated.ShortUrlProto.PageShortUrlResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ShortUrlServiceRnMethodDescriptorSupplier("getShortUrlsByUser"))
              .build();
        }
      }
    }
    return getGetShortUrlsByUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlRequest,
      edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlResponse> getShortUrlMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "shortUrl",
      requestType = edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlRequest.class,
      responseType = edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlRequest,
      edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlResponse> getShortUrlMethod() {
    io.grpc.MethodDescriptor<edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlRequest, edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlResponse> getShortUrlMethod;
    if ((getShortUrlMethod = ShortUrlServiceRnGrpc.getShortUrlMethod) == null) {
      synchronized (ShortUrlServiceRnGrpc.class) {
        if ((getShortUrlMethod = ShortUrlServiceRnGrpc.getShortUrlMethod) == null) {
          ShortUrlServiceRnGrpc.getShortUrlMethod = getShortUrlMethod =
              io.grpc.MethodDescriptor.<edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlRequest, edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "shortUrl"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ShortUrlServiceRnMethodDescriptorSupplier("shortUrl"))
              .build();
        }
      }
    }
    return getShortUrlMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ShortUrlServiceRnStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ShortUrlServiceRnStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ShortUrlServiceRnStub>() {
        @java.lang.Override
        public ShortUrlServiceRnStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ShortUrlServiceRnStub(channel, callOptions);
        }
      };
    return ShortUrlServiceRnStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ShortUrlServiceRnBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ShortUrlServiceRnBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ShortUrlServiceRnBlockingStub>() {
        @java.lang.Override
        public ShortUrlServiceRnBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ShortUrlServiceRnBlockingStub(channel, callOptions);
        }
      };
    return ShortUrlServiceRnBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ShortUrlServiceRnFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ShortUrlServiceRnFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ShortUrlServiceRnFutureStub>() {
        @java.lang.Override
        public ShortUrlServiceRnFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ShortUrlServiceRnFutureStub(channel, callOptions);
        }
      };
    return ShortUrlServiceRnFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class ShortUrlServiceRnImplBase implements io.grpc.BindableService {

    /**
     */
    public void getShortUrlsByUser(edu.pucmm.eict.grpc.generated.ShortUrlProto.RequestPageShortUrlWithUser request,
        io.grpc.stub.StreamObserver<edu.pucmm.eict.grpc.generated.ShortUrlProto.PageShortUrlResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetShortUrlsByUserMethod(), responseObserver);
    }

    /**
     */
    public void shortUrl(edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlRequest request,
        io.grpc.stub.StreamObserver<edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getShortUrlMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetShortUrlsByUserMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                edu.pucmm.eict.grpc.generated.ShortUrlProto.RequestPageShortUrlWithUser,
                edu.pucmm.eict.grpc.generated.ShortUrlProto.PageShortUrlResponse>(
                  this, METHODID_GET_SHORT_URLS_BY_USER)))
          .addMethod(
            getShortUrlMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlRequest,
                edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlResponse>(
                  this, METHODID_SHORT_URL)))
          .build();
    }
  }

  /**
   */
  public static final class ShortUrlServiceRnStub extends io.grpc.stub.AbstractAsyncStub<ShortUrlServiceRnStub> {
    private ShortUrlServiceRnStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ShortUrlServiceRnStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ShortUrlServiceRnStub(channel, callOptions);
    }

    /**
     */
    public void getShortUrlsByUser(edu.pucmm.eict.grpc.generated.ShortUrlProto.RequestPageShortUrlWithUser request,
        io.grpc.stub.StreamObserver<edu.pucmm.eict.grpc.generated.ShortUrlProto.PageShortUrlResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetShortUrlsByUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void shortUrl(edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlRequest request,
        io.grpc.stub.StreamObserver<edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getShortUrlMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ShortUrlServiceRnBlockingStub extends io.grpc.stub.AbstractBlockingStub<ShortUrlServiceRnBlockingStub> {
    private ShortUrlServiceRnBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ShortUrlServiceRnBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ShortUrlServiceRnBlockingStub(channel, callOptions);
    }

    /**
     */
    public edu.pucmm.eict.grpc.generated.ShortUrlProto.PageShortUrlResponse getShortUrlsByUser(edu.pucmm.eict.grpc.generated.ShortUrlProto.RequestPageShortUrlWithUser request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetShortUrlsByUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlResponse shortUrl(edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getShortUrlMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ShortUrlServiceRnFutureStub extends io.grpc.stub.AbstractFutureStub<ShortUrlServiceRnFutureStub> {
    private ShortUrlServiceRnFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ShortUrlServiceRnFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ShortUrlServiceRnFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.pucmm.eict.grpc.generated.ShortUrlProto.PageShortUrlResponse> getShortUrlsByUser(
        edu.pucmm.eict.grpc.generated.ShortUrlProto.RequestPageShortUrlWithUser request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetShortUrlsByUserMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlResponse> shortUrl(
        edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getShortUrlMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_SHORT_URLS_BY_USER = 0;
  private static final int METHODID_SHORT_URL = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ShortUrlServiceRnImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ShortUrlServiceRnImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_SHORT_URLS_BY_USER:
          serviceImpl.getShortUrlsByUser((edu.pucmm.eict.grpc.generated.ShortUrlProto.RequestPageShortUrlWithUser) request,
              (io.grpc.stub.StreamObserver<edu.pucmm.eict.grpc.generated.ShortUrlProto.PageShortUrlResponse>) responseObserver);
          break;
        case METHODID_SHORT_URL:
          serviceImpl.shortUrl((edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlRequest) request,
              (io.grpc.stub.StreamObserver<edu.pucmm.eict.grpc.generated.ShortUrlProto.ShortUrlResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ShortUrlServiceRnBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ShortUrlServiceRnBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return edu.pucmm.eict.grpc.generated.ShortUrlProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ShortUrlServiceRn");
    }
  }

  private static final class ShortUrlServiceRnFileDescriptorSupplier
      extends ShortUrlServiceRnBaseDescriptorSupplier {
    ShortUrlServiceRnFileDescriptorSupplier() {}
  }

  private static final class ShortUrlServiceRnMethodDescriptorSupplier
      extends ShortUrlServiceRnBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ShortUrlServiceRnMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ShortUrlServiceRnGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ShortUrlServiceRnFileDescriptorSupplier())
              .addMethod(getGetShortUrlsByUserMethod())
              .addMethod(getShortUrlMethod())
              .build();
        }
      }
    }
    return result;
  }
}
